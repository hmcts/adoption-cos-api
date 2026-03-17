# Adoption COS API - Architecture & Patterns Analysis

## Overview
The adoption-cos-api is a Case Management System API built with Spring Boot and heavily integrated with the CCD (Common Case Data) SDK. It follows a **task-based reactive pattern** combined with Spring event publishing for notification handling.

---

## 1. CCD Callback Pattern

### Structure & Annotations
All CCD events implement `CCDConfig<CaseData, State, UserRole>` and are Spring `@Component`s.

**Key CCD Callback Types:**
- `aboutToSubmitCallback()` - Validation & data transformation before submission
- `submittedCallback()` - Post-submission async operations (event publishing)

### Example: CitizenCreateApplication.java
```java
@Component
@Slf4j
public class CitizenCreateApplication implements CCDConfig<CaseData, State, UserRole> {
    public static final String CITIZEN_CREATE = "citizen-create-application";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(CITIZEN_CREATE)
            .initialState(Draft)
            .name("Create adoption draft case")
            .description("Apply for adoption")
            .ttlIncrement(90)
            .aboutToSubmitCallback(this::aboutToSubmit)  // Pre-submission hook
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .retries(120, 120);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails) {
        log.info("Citizen create adoption application about to submit callback invoked");
        CaseData data = details.getData();
        // Transform data, set defaults, format case reference
        data.setHyphenatedCaseRef(String.format("%4s-%4s-%4s-%4s", ...));
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .build();
    }
}
```

### Event Registration Pattern
Events are wired via `ConfigBuilder`:
- `.event(eventId)` - Registers event ID (e.g., "citizen-create-application")
- `.forStates(State...)` - Applies to specific states or all states
- `.forState(State)` - Single state variant
- `.grant(Permissions, UserRole...)` - Access control
- `.retries(120, 120)` - Retry configuration

**Example with submitted callback:**
```java
// CitizenAddPayment.java
configBuilder
    .event(CITIZEN_ADD_PAYMENT)
    .forState(AwaitingPayment)
    .name("Payment made")
    .aboutToSubmitCallback(this::aboutToSubmit)
    .submittedCallback(this::submitted);

public SubmittedCallbackResponse submitted(CaseDetails<CaseData, State> details,
                                          CaseDetails<CaseData, State> beforeDetails) {
    if (EnumSet.of(Submitted).contains(details.getState())) {
        eventPublisher.publishEvent(
            ApplicationSubmitNotificationEvent.builder()
                .caseData(details)
                .build());
    }
    return SubmittedCallbackResponse.builder().build();
}
```

---

## 2. Task Pattern

### Core Abstraction: CaseTask Interface
All tasks implement a simple functional interface:

```java
public interface CaseTask extends Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>> {
}
```

Tasks are pure functions that transform case details.

### Task Composition: CaseTaskRunner
Tasks are composed using a **function pipeline** pattern:

```java
public final class CaseTaskRunner {
    private final Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>> caseTask;

    @SafeVarargs
    public static CaseTaskRunner caseTasks(
        final Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>>... tasks) {
        return new CaseTaskRunner(of(tasks).reduce(identity(), Function::andThen));
    }

    public CaseDetails<CaseData, State> run(final CaseDetails<CaseData, State> caseDetails) {
        return caseTask.apply(caseDetails);
    }
}
```

### Task Registration in Services
Tasks are registered and composed in Service classes (dependency injection):

```java
@Service
public class SubmissionService {
    @Autowired
    private SetStateAfterSubmission setStateAfterSubmission;
    
    @Autowired
    private SetDateSubmitted setDateSubmitted;
    
    @Autowired
    private GenerateApplicationSummaryDocument generateApplicationSummaryDocument;

    public CaseDetails<CaseData, State> submitApplication(
        final CaseDetails<CaseData, State> caseDetails) {
        return CaseTaskRunner.caseTasks(
            setStateAfterSubmission,
            setDateSubmitted,
            generateApplicationSummaryDocument
        ).run(caseDetails);
    }
}
```

### Task Examples

**SetStateAfterSubmission (Conditional State Logic):**
```java
@Component
@Slf4j
public class SetStateAfterSubmission implements CaseTask {
    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Application application = caseData.getApplication();

        if (!application.hasBeenPaidFor()) {
            caseDetails.setState(AwaitingPayment);
        } else {
            caseDetails.setState(Submitted);
        }
        caseDetails.getData().setStatus(Submitted);
        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());
        return caseDetails;
    }
}
```

**GenerateApplicationSummaryDocument (Async Document Generation):**
```java
@Component
@Slf4j
public class GenerateApplicationSummaryDocument implements CaseTask {
    @Autowired
    private CaseDataDocumentService caseDataDocumentService;

    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();

        if (EnumSet.of(Submitted).contains(caseDetails.getState())) {
            log.info("Generating summary document for caseId: {}", caseId);
            Map<String, Object> templateContent = objectMapper.convertValue(caseData, Map.class);
            
            // Generate English or Welsh document based on language preference
            final CompletableFuture<Void> appSummary = caseData.getApplicant1()
                .getLanguagePreference().equals(LanguagePreference.ENGLISH)
                ? generateDocument(caseData, APPLICATION_SUMMARY_EN, templateContent, ...)
                : generateDocument(caseData, APPLICATION_SUMMARY_CY, templateContent, ...);
            
            CompletableFuture.allOf(appSummary).join();
        }
        return caseDetails;
    }
}
```

### Locations
- **adoptioncase/task/** - Core case task implementations
- **service/task/** - Service-level tasks (EventService, ScheduledTaskRunner)
- **document/task/** - Document generation tasks
- **common/service/task/** - Reusable tasks (SetState, SetDate, GenerateDocuments, SendNotifications)

---

## 3. Event Handlers

### Event Publishing Architecture
Uses Spring's **ApplicationEventPublisher**:

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(Object event) {
        log.info("Publishing event {}", event.getClass().getSimpleName());
        applicationEventPublisher.publishEvent(event);
    }
}
```

### Custom Event Records
Events are defined as immutable records (Java 16+):

```java
// service/event/ApplicationSubmitNotificationEvent.java
@Builder
public record ApplicationSubmitNotificationEvent(CaseDetails<CaseData, State> caseData) { }

// service/event/LocalAuthorityApplicationSubmitNotificationEvent.java
@Builder
public record LocalAuthorityApplicationSubmitNotificationEvent(CaseDetails<CaseData, State> caseData) { }
```

### Event Listeners
Handlers use `@EventListener` annotation (Spring event async processing):

```java
@Slf4j
@Component
public class ApplicationSubmittedNotificationEventHandler {
    private final SendNotificationService sendNotificationService;

    @EventListener
    public void sendNotificationPostApplicationSubmission(
        ApplicationSubmitNotificationEvent applicationSubmitNotificationEvent) {
        Long caseId = applicationSubmitNotificationEvent.caseData().getId();
        log.info("ApplicationSubmittedNotificationEventHandler triggered for CaseID: {}", caseId);
        sendNotificationService.sendNotifications(
            applicationSubmitNotificationEvent.caseData());
    }
}
```

### CCD Event Configuration
Events are registered in `adoptioncase/event/` directory (implements CCDConfig):

**Example: CitizenSubmitApplication**
```java
@Component
@Slf4j
public class CitizenSubmitApplication implements CCDConfig<CaseData, State, UserRole> {
    public static final String CITIZEN_SUBMIT = "citizen-submit-application";

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(CITIZEN_SUBMIT)
            .forStates(Draft, AwaitingPayment)
            .name("Applicant Statement of Truth")
            .aboutToSubmitCallback(this::aboutToSubmit);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> beforeDetails) {
        // Validation
        final List<String> validationErrors = validateReadyForPayment(data);
        if (!validationErrors.isEmpty()) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(data)
                .errors(validationErrors)
                .state(state)
                .build();
        }
        // State transition
        state = AwaitingPayment;
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .state(state)
            .build();
    }
}
```

**Example: LocalAuthoritySubmitApplication with Event Publishing**
```java
@Component
@Slf4j
public class LocalAuthoritySubmitApplication implements CCDConfig<CaseData, State, UserRole> {
    @Autowired private SubmissionService submissionService;
    @Autowired private EventService eventPublisher;

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(LOCAL_AUTHORITY_SUBMIT)
            .forStates(Submitted)
            .aboutToSubmitCallback(this::aboutToSubmit)
            .submittedCallback(this::submitted);
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails) {
        final Long caseId = details.getId();
        log.info("Local Authority Submit about to submit callback invoked CaseID: {}", caseId);
        final CaseDetails<CaseData, State> updatedCaseDetails = 
            submissionService.laSubmitApplication(details);
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(updatedCaseDetails.getData())
            .state(LaSubmitted)
            .build();
    }

    public SubmittedCallbackResponse submitted(CaseDetails<CaseData, State> details, 
                                               CaseDetails<CaseData, State> beforeDetails) {
        log.info("Local Authority Submit submitted callback invoked CaseID: {}", details.getId());
        eventPublisher.publishEvent(
            LocalAuthorityApplicationSubmitNotificationEvent.builder()
                .caseData(details)
                .build());
        return SubmittedCallbackResponse.builder().build();
    }
}
```

---

## 4. Notification Pattern

### Architecture: Notification Components
1. **Notification Classes** (`@Component`) - Template rendering & dispatch logic
2. **NotificationDispatcher** (`@Service`) - Routes notifications to recipients
3. **SendgridService** (`@Service`) - Email provider integration
4. **Event Handlers** (`@EventListener`) - Async notification triggers

### Notification Template Pattern
```java
@Component
@Slf4j
public class ApplicationSubmittedNotification extends ApplicantNotification {
    @Autowired private IdamService idamService;
    @Autowired private SendgridService sendgridService;
    @Autowired private CaseDocumentClient caseDocumentClient;

    public void sendToApplicants(CaseData caseData, Long caseId) 
        throws NotificationClientException, IOException {
        
        Applicant applicant1 = caseData.getApplicant1();
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(APPLICANT_1_FULL_NAME, applicant1.getFirstName() + " " + applicant1.getLastName());
        templateVars.put(APPLICATION_REFERENCE, caseData.getHyphenatedCaseRef());
        templateVars.put(SUBMISSION_RESPONSE_DATE, 
            caseData.getApplication().getDateOfSubmissionResponse().format(DATE_TIME_FORMATTER));
        
        sendgridService.sendEmail(
            applicant1.getEmailAddress(),
            APPLICANT_APPLICATION_SUBMITTED.template(),
            templateVars
        );
    }

    public void sendToLocalCourt(CaseData caseData, Long caseId) 
        throws NotificationClientException, IOException {
        // Court notification logic
    }

    public void sendToLocalAuthorityPostApplicantSubmission(CaseData caseData, Long caseId) 
        throws NotificationClientException, IOException {
        // LA notification logic
    }
}
```

### Notification Dispatcher (Router)
```java
@Service
@Slf4j
public class NotificationDispatcher {
    public void send(final ApplicantNotification applicantNotification, 
                    final CaseData caseData, final Long caseId)
        throws NotificationClientException, IOException {
        if (!caseData.getApplicant1().getEmailAddress().isEmpty()) {
            applicantNotification.sendToApplicants(caseData, caseId);
            try {
                applicantNotification.sendToLocalCourt(caseData, caseId);
                applicantNotification.sendToLocalAuthorityPostApplicantSubmission(caseData, caseId);
            } catch (Exception e) {
                log.error("Exception occurred in send method: {}", e);
            }
        }
    }
}
```

### Event-Driven Notification Trigger
```java
@Slf4j
@Component
public class ApplicationSubmittedNotificationEventHandler {
    private final SendNotificationService sendNotificationService;

    @EventListener
    public void sendNotificationPostApplicationSubmission(
        ApplicationSubmitNotificationEvent applicationSubmitNotificationEvent) {
        Long caseId = applicationSubmitNotificationEvent.caseData().getId();
        log.info("Triggered for CaseID: {}", caseId);
        sendNotificationService.sendNotifications(
            applicationSubmitNotificationEvent.caseData());
    }
}
```

### Sendgrid Integration
```java
@Service
@Slf4j
public class SendgridService {
    @Autowired private SendGridClient sendGridClient;

    public void sendEmail(String email, String templateId, Map<String, Object> personalisation) 
        throws NotificationClientException, IOException {
        SendGridRequest sendGridRequest = SendGridRequest.builder()
            .to(email)
            .templateId(templateId)
            .personalisation(personalisation)
            .build();
        
        sendGridClient.sendEmail(sendGridRequest);
    }
}
```

### Notification Task (Used in Submission Pipeline)
```java
@Component
@Slf4j
public class SendSubmissionNotifications implements CaseTask {
    @Autowired
    private ApplicationSubmittedNotification applicationSubmittedNotification;
    
    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();

        if (EnumSet.of(Submitted).contains(state)) {
            log.info("Sending application submitted notifications for case : {}", caseId);
            try {
                notificationDispatcher.send(applicationSubmittedNotification, caseData, caseId);
            } catch (NotificationClientException | IOException e) {
                log.error("Couldn't send notifications");
            }
        }
        return caseDetails;
    }
}
```

### Notification Locations
- **notification/** - Main notification classes
- **notification/handlers/** - Event listeners for async triggers
- **common/notification/** - Shared notification base classes
- **common/service/SendNotificationService** - Orchestration service

---

## 5. CCD Model/Config Generation

### CCD Model with @CCD Annotations
CaseData uses CCD SDK annotations to define field metadata:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseData {
    
    @CCD(
        label = "Applying with",
        access = {DefaultAccess.class},
        typeOverride = FixedRadioList,
        typeParameterOverride = "ApplyingWith"
    )
    private ApplyingWith applyingWith;

    @CCD(
        label = "Are the applicants represented by a solicitor?",
        access = {DefaultAccess.class}
    )
    private YesOrNo isApplicantRepresentedBySolicitor;

    @CCD(
        label = "Date child moved in",
        access = {DefaultAccess.class}
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateChildMovedIn;

    @CCD(
        label = "Applying with someone else reason",
        typeOverride = TextArea,
        access = {DefaultAccess.class}
    )
    private String otherApplicantRelation;

    // Complex nested types with collections
    @CCD(label = "Payment Method", 
         typeOverride = Collection, 
         typeParameterOverride = "Payment", 
         access = { CollectionAccess.class })
    private List<ListValue<Payment>> applicationPayments;
}
```

### Access Control
Access is controlled via specialized access classes:
- `DefaultAccess` - All authenticated users
- `CaseworkerAccess` - Only case workers
- `SystemUpdateAccess` - System update user only
- `CollectionAccess` - Collection-specific access
- `TtlAccess` - Time-to-live field access

### Tabs Configuration
```java
@Component
public class CaseTypeTab implements CCDConfig<CaseData, State, UserRole> {
    
    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        buildCfvTab(configBuilder);
        buildSummaryTab(configBuilder);
        buildApplicantsTab(configBuilder);
        buildDocumentsTab(configBuilder);
    }

    private void buildCfvTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("cfv", "Case File View")
            .forRoles(CASE_WORKER)
            .field(CaseData::getCaseFileView, null, "#ARGUMENT(CaseFileView)");
    }

    public void buildApplicantsTab(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("applicationDetails", "Applicants")
            .showCondition(TabShowCondition.showForState(State.Submitted, State.LaSubmitted))
            .label("LabelApplicant-Heading",
                   "applyingWith=\"alone\"",
                   "# Applicant")
            .label("LabelApplicants-Heading",
                   "applyingWith!=\"alone\"",
                   "# Applicants")
            .field("applyingWith", "applyingWith=\"applyingWith\"")
            .field("applicant1FirstName")
            .field("applicant1LastName")
            .field("applicant1DateOfBirth");
    }
}
```

### Workbasket Configuration
```java
@Component
public class WorkBasketInputFields implements CCDConfig<CaseData, State, UserRole> {
    
    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .workBasketInputFields()
            .field("hyphenatedCaseRef", "Case reference number")
            .field("childrenFirstName", "Child's first name")
            .field("childrenLastName", "Child's last name")
            .field("dateSubmitted", "Date submitted");
    }
}
```

### CCD Configuration Locations
- **adoptioncase/model/** - Core CaseData and domain models with @CCD annotations
- **adoptioncase/model/access/** - Access control classes
- **adoptioncase/tab/** - Tab configurations
- **adoptioncase/workbasket/** - Workbasket/search input fields
- **adoptioncase/event/** - Event configurations (implements CCDConfig)

---

## 6. Test Conventions

### Base Test Class
```java
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Adoption.class)
@TestPropertySource(locations = "/application-contract.properties")
public abstract class BaseTest {
    @MockitoBean
    protected AuthTokenGenerator authTokenGenerator;
}
```

### Unit Tests
Use **Mockito** with `@ExtendWith(MockitoExtension.class)`:

```java
public class ApplicationSubmittedNotificationTest {
    
    @Mock
    private SendgridService sendgridService;
    
    @Mock
    private CaseDocumentClient caseDocumentClient;
    
    @InjectMocks
    private ApplicationSubmittedNotification notification;

    @Test
    void testSendToApplicants() throws NotificationClientException, IOException {
        // Setup test data
        CaseData caseData = caseData();
        Long caseId = 1234567890123456L;

        // Stub mocks
        when(sendgridService.sendEmail(...)).doNothing();
        
        // Execute
        notification.sendToApplicants(caseData, caseId);

        // Verify
        verify(sendgridService, times(1)).sendEmail(
            eq("test@example.com"),
            eq(APPLICANT_APPLICATION_SUBMITTED.template()),
            any(Map.class)
        );
    }
}
```

### Functional Tests
Integration tests extending `FunctionalTest`:

```java
@Slf4j
@TestPropertySource("classpath:application.yaml")
public abstract class FunctionalTest {
    @Autowired protected IdamTokenGenerator idamTokenGenerator;
    @Autowired protected CoreCaseDataApi coreCaseDataApi;
    @Autowired protected ServiceAuthenticationGenerator serviceAuthenticationGenerator;

    protected CaseDetails createCaseInCcd() {
        String userToken = idamTokenGenerator.generateIdamTokenForSystem();
        String s2sTokenForCosApi = serviceAuthenticationGenerator.generate("adoption_cos_api");
        // Create case via CCD API
    }

    protected Response triggerCallback(Map<String, Object> caseData, String eventId, String url) 
        throws IOException {
        CallbackRequest request = CallbackRequest.builder()
            .eventId(eventId)
            .caseDetailsBefore(...)
            .caseDetails(...)
            .build();
        return triggerCallback(request, url);
    }
}
```

### Test Utilities
```java
public class TestDataHelper {
    public static final LocalDate LOCAL_DATE = LocalDate.of(2021, 4, 28);

    public static ListValue<AdoptionDocument> documentWithType(final DocumentType documentType) {
        Document ccdDocument = new Document(
            "http://localhost:8080/" + UUID.randomUUID().toString(),
            "test-draft-adoption-application.pdf",
            "..."
        );
        AdoptionDocument adoptionDocument = AdoptionDocument.builder()
            .documentLink(ccdDocument)
            .documentType(documentType)
            .build();
        return ListValue.<AdoptionDocument>builder()
            .value(adoptionDocument)
            .build();
    }

    public static CaseData caseData() {
        // Factory method for test case data
    }

    public static Map<String, Object> getMainTemplateVars(CaseData caseData) {
        // Extract template variables for notifications
    }
}
```

### Test Structure
- **Unit tests** use `@ExtendWith(MockitoExtension.class)` with mocked dependencies
- **Contract/Integration tests** extend `BaseTest` with Spring context
- **Functional tests** extend `FunctionalTest` with CCD API integration
- **Test data** centralized in `testutil/TestDataHelper.java`

---

## 7. LaunchDarkly Feature Flagging

### LaunchDarkly Client
```java
@Configuration
@Service
@Slf4j
public class LaunchDarklyClient {
    public static final LDUser ADOPTION_COS_USER = 
        new LDUser.Builder("adoption-cos-api")
            .anonymous(true)
            .build();

    private final LDClientInterface internalClient;

    @Autowired
    public LaunchDarklyClient(
        LaunchDarkClientFactory ldClientFactory,
        @Value("${launchdarkly.sdk-key:}") String sdkKey,
        @Value("${launchdarkly.offline-mode:false}") Boolean offlineMode) {
        this.internalClient = ldClientFactory.create(sdkKey, offlineMode);
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public boolean isFeatureEnabled(String feature) {
        return internalClient.boolVariation(feature, ADOPTION_COS_USER, false);
    }

    public boolean isFeatureEnabled(String feature, LDUser user) {
        return internalClient.boolVariation(feature, user, false);
    }

    private void close() {
        try {
            internalClient.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
```

### Factory Pattern
```java
@Service
public class LaunchDarkClientFactory {
    public LDClientInterface create(String sdkKey, Boolean offlineMode) {
        LDConfig config = new LDConfig.Builder()
            .offline(offlineMode)
            .build();
        return new LDClient(sdkKey, config);
    }
}
```

### Configuration
- **config.launchdarkly** - Feature flag client factory
- Properties: `launchdarkly.sdk-key` and `launchdarkly.offline-mode`

---

## 8. Key Integration Points

### IDAM (Identity & Access Management)
```java
@Service
public class IdamService {
    @Value("${idam.systemupdate.username}")
    private String systemUpdateUserName;

    @Autowired
    private IdamClient idamClient;

    public User retrieveUser(String authorisation) {
        final String bearerToken = getBearerToken(authorisation);
        final UserDetails userDetails = idamClient.getUserDetails(bearerToken);
        return new User(bearerToken, userDetails);
    }

    public User retrieveSystemUpdateUserDetails() {
        return retrieveUser(getIdamOauth2Token(systemUpdateUserName, systemUpdatePassword));
    }
}
```

### CCD (Case Management Data)
- Uses `CoreCaseDataApi` client for case operations
- CCDConfig components auto-register with CCD system
- Case callbacks (aboutToSubmit, submitted) handled via CCD SDK

### Document Management (DocAssembly/Docmosis)
```java
@Service
@Slf4j
public class DocAssemblyService {
    @Autowired private DocAssemblyClient docAssemblyClient;
    @Autowired private DocmosisTemplateProvider docmosisTemplateProvider;

    public DocumentInfo renderDocument(
        final Map<String, Object> templateContent,
        final Long caseId,
        final String authorisation,
        final String templateId,
        final LanguagePreference languagePreference,
        final String filename) {
        
        final String templateName = docmosisTemplateProvider
            .templateNameFor(templateId, languagePreference);
        final DocAssemblyRequest docAssemblyRequest = DocAssemblyRequest.builder()
            .templateId(templateName)
            .outputType(PDF)
            .caseTypeId(CASE_TYPE)
            .data(templateContent)
            .build();
        
        return docAssemblyClient.generateDocument(
            docAssemblyRequest, caseId, authorisation);
    }
}
```

### Payment Service
- **Payment Model**: Track payment status (SUCCESS, IN_PROGRESS, FAILED)
- **Application integration**: Check `application.hasBeenPaidFor()` before proceeding
- **State transitions**: AwaitingPayment → Submitted upon successful payment

```java
public class Application {
    @JsonIgnore
    public PaymentStatus getLastPaymentStatus() { ... }
    
    @JsonIgnore
    public boolean hasBeenPaidFor() {
        return null != applicationFeeOrderSummary
                && Integer.parseInt(applicationFeeOrderSummary.getPaymentTotal()) 
                   == getPaymentTotal();
    }
}
```

### Notification Service (SendGrid)
```java
@Service
@Slf4j
public class SendgridService {
    public void sendEmail(String email, String templateId, 
                         Map<String, Object> personalisation) 
        throws NotificationClientException, IOException {
        // Send via SendGrid
    }
}
```

### CCD Search Service
```java
@Service
@Slf4j
public class CcdSearchService {
    @Autowired
    private CoreCaseDataApi coreCaseDataApi;

    public List<CaseDetails> searchCases(
        String authorisation, String s2sToken, 
        String query, String caseType) {
        // Elasticsearch query execution
    }
}
```

---

## 9. SystemUpdate Package

### Purpose
Handles system-initiated operations outside normal user workflows (e.g., automated tasks, scheduled jobs).

### CaseDetailsConverter
Converts between CCD SDK model and Reform CCD model:

```java
@Component
public class CaseDetailsConverter {
    @Autowired
    private ObjectMapper objectMapper;

    public uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State> 
        convertToCaseDetailsFromReformModel(final CaseDetails caseDetails) {
        return objectMapper.convertValue(caseDetails, 
            new TypeReference<uk.gov.hmcts.ccd.sdk.api.CaseDetails<CaseData, State>>() {
        });
    }
}
```

### Usage
- Scheduled tasks that need to read/update cases
- System-initiated workflows (e.g., bulk operations)
- Conversion between external API models and internal SDK models

---

## 10. SetState Tasks (common/service/task/)

### SetStateAfterSubmission
Applies conditional logic to determine next state based on payment:

```java
@Component
@Slf4j
public class SetStateAfterSubmission implements CaseTask {
    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        final CaseData caseData = caseDetails.getData();
        final Application application = caseData.getApplication();

        // Payment-driven state transition
        if (!application.hasBeenPaidFor()) {
            caseDetails.setState(AwaitingPayment);
        } else {
            caseDetails.setState(Submitted);
        }

        caseDetails.getData().setStatus(Submitted);
        log.info("State set to {}, CaseID {}", 
            caseDetails.getState(), caseDetails.getId());
        return caseDetails;
    }
}
```

### SetStateAfterLaSubmission
Local Authority submission state transition:

```java
@Component
@Slf4j
public class SetStateAfterLaSubmission implements CaseTask {
    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        log.info("Setting state to LaSubmitted for CaseID: {}", caseDetails.getId());
        caseDetails.setState(LaSubmitted);
        caseDetails.getData().setStatus(LaSubmitted);
        return caseDetails;
    }
}
```

### SetDateSubmitted
Captures submission timestamp:

```java
@Component
@Slf4j
public class SetDateSubmitted implements CaseTask {
    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        LocalDate now = LocalDate.now(clock);
        caseDetails.getData().getApplication().setDateSubmitted(now);
        log.info("Date submitted set for CaseID: {}", caseDetails.getId());
        return caseDetails;
    }
}
```

### Composition Example
```java
public CaseDetails<CaseData, State> submitApplication(
    final CaseDetails<CaseData, State> caseDetails) {
    return CaseTaskRunner.caseTasks(
        setStateAfterSubmission,    // Step 1: Determine state
        setDateSubmitted,           // Step 2: Set timestamp
        generateApplicationSummaryDocument  // Step 3: Generate PDF
    ).run(caseDetails);
}
```

---

## Key Architectural Principles

### 1. **Functional Composition**
- Tasks implemented as pure functions: `Function<CaseDetails, CaseDetails>`
- Composed via `CaseTaskRunner` using `Function.andThen()`
- No side effects within tasks themselves

### 2. **Event-Driven Async**
- Submit callbacks trigger Spring events (`@EventListener`)
- Handlers execute asynchronously for notifications, document generation
- Decouples synchronous CCD operations from async side effects

### 3. **Dependency Injection**
- All Spring components autowired
- Task dependencies injected into Service classes
- Enables testing via mocking and composition

### 4. **CCD SDK Integration**
- CCDConfig implementations auto-discovered by Spring
- Annotations drive form generation and access control
- Callbacks provide extension points for business logic

### 5. **Explicit State Management**
- Clear State enum (Draft, Submitted, LaSubmitted, AwaitingPayment, etc.)
- State transitions explicit in tasks
- All state changes logged

### 6. **Template-Driven Document Generation**
- Docmosis for PDF rendering
- Language-aware template selection (English/Welsh)
- Async document generation via CompletableFuture

### 7. **Multi-Channel Notifications**
- SendGrid for email
- Multiple recipient types: Applicants, Courts, Local Authorities
- Template variables centralized in CommonContent

---

## Directory Structure Summary

```
src/main/java/uk/gov/hmcts/reform/adoption/
├── adoptioncase/
│   ├── event/              # CCD event configurations
│   ├── caseworker/event/   # Caseworker-specific events
│   ├── model/              # Domain models with @CCD annotations
│   ├── tab/                # Tab configurations
│   ├── workbasket/         # Search/workbasket fields
│   ├── task/               # Case-level tasks
│   ├── schedule/           # Scheduled tasks
│   └── service/            # Domain services (CcdSearchService, etc.)
├── service/
│   ├── event/              # Event records (ApplicationSubmitNotificationEvent)
│   ├── task/               # EventService, ScheduledTaskRunner
├── common/
│   ├── service/
│   │   ├── task/           # Reusable tasks (SetState, SetDate, Generate, Send)
│   │   └── SubmissionService  # Task composition
│   ├── config/             # EmailTemplatesConfig, DocmosisTemplatesConfig
│   └── notification/       # Base notification classes
├── notification/
│   ├── handlers/           # @EventListener event handlers
│   ├── NotificationDispatcher
│   ├── SendgridService
│   └── [Notification classes]
├── document/
│   ├── task/               # Document generation tasks
│   ├── DocAssemblyService
│   ├── CaseDataDocumentService
│   └── model/
├── config/
│   └── launchdarkly/       # LaunchDarkly client factory
├── idam/                   # IDAM user service
├── systemupdate/           # System conversion utilities
└── controllers/            # REST endpoints (minimal)
```

---

## Key Takeaways for Copilot Instructions

1. **Tasks are composable functions** that transform case details - use CaseTaskRunner for pipelines
2. **Events decouple submission from notifications** - publish in submittedCallback, listen with @EventListener
3. **All CCD config extends CCDConfig** and uses ConfigBuilder fluent API
4. **State is explicit and enumerated** - SetState tasks manage transitions
5. **Notifications are template-driven** with multi-channel dispatch (ApplicantNotification, NotificationDispatcher)
6. **Document generation is async** using CompletableFuture within tasks
7. **LaunchDarkly client is injected** as a service for feature flags
8. **External integrations** (IDAM, CCD, DocAssembly, SendGrid) wrapped in dedicated services
9. **Tests use Mockito** with @ExtendWith(MockitoExtension.class) for unit tests
10. **Functional tests extend FunctionalTest** with CCD API integration helpers
