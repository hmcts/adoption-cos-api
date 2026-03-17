# Adoption COS API - Copilot Development Instructions

## Quick Reference

This document provides AI Copilot with essential patterns and conventions for developing in adoption-cos-api. Refer to [ARCHITECTURE.md](./ARCHITECTURE.md) for detailed explanations with code examples.

---

## 1. Creating a New CCD Event

**Pattern:** Implement `CCDConfig<CaseData, State, UserRole>` as `@Component`

### Template
```java
@Component
@Slf4j
public class MyNewEvent implements CCDConfig<CaseData, State, UserRole> {
    
    public static final String EVENT_ID = "my-new-event";
    
    @Autowired
    private MyService myService; // Inject dependencies

    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder
            .event(EVENT_ID)
            .forState(Draft)  // or .forStates(Draft, AwaitingPayment) for multiple
            .name("User-visible event name")
            .description("Description shown in UI")
            .ttlIncrement(90)  // Optional: time-to-live
            .grant(CREATE_READ_UPDATE, CITIZEN)
            .grant(READ, CASE_WORKER)
            .retries(120, 120)
            .aboutToSubmitCallback(this::aboutToSubmit);  // Optional
            // .submittedCallback(this::submitted);  // Optional
    }

    public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(
        final CaseDetails<CaseData, State> details,
        final CaseDetails<CaseData, State> beforeDetails) {
        
        log.info("Event {} about to submit callback invoked for CaseID: {}", 
            EVENT_ID, details.getId());
        
        CaseData data = details.getData();
        State state = details.getState();
        
        // Validation
        List<String> errors = new ArrayList<>();
        if (data.getSomeField() == null) {
            errors.add("Some field is required");
        }
        
        if (!errors.isEmpty()) {
            return AboutToStartOrSubmitResponse.<CaseData, State>builder()
                .data(data)
                .state(state)
                .errors(errors)
                .build();
        }
        
        // Data transformation
        data.setProcessedField(processField(data));
        
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(data)
            .state(state)  // Can change state here
            .build();
    }

    // Optional: Called after case is submitted
    public SubmittedCallbackResponse submitted(
        CaseDetails<CaseData, State> details,
        CaseDetails<CaseData, State> beforeDetails) {
        
        log.info("Submitted callback invoked for CaseID: {}", details.getId());
        // Async operations here (e.g., publish events)
        return SubmittedCallbackResponse.builder().build();
    }
}
```

### Key Points
- Always use `@Component` + `@Slf4j`
- Event ID convention: lowercase with hyphens (e.g., "citizen-submit-application")
- aboutToSubmit: Validation + synchronous data transformation
- submitted: Async operations, event publishing (no response data returned)
- Use ConfigBuilder fluent API for configuration

---

## 2. Creating a New Task

**Pattern:** Implement `CaseTask` (a Function interface), annotate with `@Component`

### Single Task
```java
@Component
@Slf4j
public class MyNewTask implements CaseTask {
    
    @Autowired
    private SomeDependency someDependency;

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        final CaseData data = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        final State state = caseDetails.getState();
        
        log.info("Executing MyNewTask for CaseID: {}, State: {}", caseId, state);
        
        // Perform transformation
        data.setNewField(calculateValue(data));
        
        return caseDetails;
    }
}
```

### Composing Tasks (Pipelines)
```java
@Service
public class MySubmissionService {
    
    @Autowired private Task1 task1;
    @Autowired private Task2 task2;
    @Autowired private Task3 task3;
    
    public CaseDetails<CaseData, State> processCase(
        final CaseDetails<CaseData, State> caseDetails) {
        
        return CaseTaskRunner.caseTasks(
            task1,  // Executed first
            task2,  // Executed second, receives output from task1
            task3   // Executed third, receives output from task2
        ).run(caseDetails);
    }
}
```

### Typical Task Locations
- **SetState tasks**: common/service/task/ (manage state transitions)
- **Document generation**: common/service/task/ or document/task/
- **Data validation/transformation**: common/service/task/
- **Notifications**: common/service/task/SendSubmissionNotifications

---

## 3. Publishing Events & Notification Handlers

**Pattern:** Publish Spring events in `submittedCallback`, listen with `@EventListener`

### Define Event Record
```java
// service/event/MyCustomEvent.java
@Builder
public record MyCustomEvent(CaseDetails<CaseData, State> caseData) { }
```

### Publish Event (in submittedCallback)
```java
@Autowired
private EventService eventPublisher;

public SubmittedCallbackResponse submitted(
    CaseDetails<CaseData, State> details,
    CaseDetails<CaseData, State> beforeDetails) {
    
    log.info("Publishing MyCustomEvent for CaseID: {}", details.getId());
    eventPublisher.publishEvent(
        MyCustomEvent.builder()
            .caseData(details)
            .build());
    
    return SubmittedCallbackResponse.builder().build();
}
```

### Listen to Event (Async Handler)
```java
// notification/handlers/MyEventHandler.java
@Slf4j
@Component
public class MyEventHandler {
    
    @Autowired
    private MyService myService;
    
    @EventListener
    public void handleMyCustomEvent(MyCustomEvent event) {
        Long caseId = event.caseData().getId();
        log.info("MyEventHandler triggered for CaseID: {}", caseId);
        
        try {
            myService.doSomethingAsync(event.caseData());
        } catch (Exception e) {
            log.error("Error handling event", e);
        }
    }
}
```

---

## 4. Creating Notifications

**Pattern:** Extend `ApplicantNotification`, use `NotificationDispatcher` and `SendgridService`

### Notification Class
```java
@Component
@Slf4j
public class MyNotification extends ApplicantNotification {
    
    @Autowired
    private SendgridService sendgridService;
    
    @Autowired
    private IdamService idamService;
    
    @Autowired
    private CaseDocumentClient caseDocumentClient;
    
    public void sendToApplicants(CaseData caseData, Long caseId) 
        throws NotificationClientException, IOException {
        
        Applicant applicant1 = caseData.getApplicant1();
        Map<String, Object> templateVars = new HashMap<>();
        
        // Add template variables
        templateVars.put("applicant_name", applicant1.getFirstName());
        templateVars.put("case_reference", caseData.getHyphenatedCaseRef());
        templateVars.put("submission_date", 
            caseData.getApplication().getDateSubmitted());
        
        // Send email
        sendgridService.sendEmail(
            applicant1.getEmailAddress(),
            EmailTemplateName.MY_TEMPLATE.template(),
            templateVars
        );
    }
    
    public void sendToLocalCourt(CaseData caseData, Long caseId) 
        throws NotificationClientException, IOException {
        // Court notification logic
    }
}
```

### Trigger Notification via Task
```java
@Component
@Slf4j
public class MyNotificationTask implements CaseTask {
    
    @Autowired
    private MyNotification myNotification;
    
    @Autowired
    private NotificationDispatcher dispatcher;
    
    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        final CaseData data = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        
        log.info("Sending notifications for CaseID: {}", caseId);
        
        try {
            dispatcher.send(myNotification, data, caseId);
        } catch (NotificationClientException | IOException e) {
            log.error("Failed to send notifications", e);
        }
        
        return caseDetails;
    }
}
```

---

## 5. Defining CCD Fields with Annotations

**Pattern:** Use `@CCD` annotation with metadata on CaseData fields

### Basic Field
```java
@CCD(
    label = "First Name",
    access = {DefaultAccess.class}
)
private String firstName;
```

### Field with Type Override
```java
@CCD(
    label = "Description",
    access = {DefaultAccess.class},
    typeOverride = TextArea
)
private String description;
```

### Radio List Field
```java
@CCD(
    label = "Applying with",
    access = {DefaultAccess.class},
    typeOverride = FixedRadioList,
    typeParameterOverride = "ApplyingWith"
)
private ApplyingWith applyingWith;
```

### Collection Field
```java
@CCD(
    label = "Documents",
    typeOverride = Collection,
    typeParameterOverride = "AdoptionDocument",
    access = {CollectionAccess.class}
)
private List<ListValue<AdoptionDocument>> documents;
```

### Date Field
```java
@CCD(
    label = "Date Submitted",
    access = {DefaultAccess.class}
)
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate dateSubmitted;
```

### Access Control Options
- `DefaultAccess` - All authenticated users
- `CaseworkerAccess` - Case workers only
- `SystemUpdateAccess` - System user only
- `CollectionAccess` - Collection-specific access

---

## 6. Building Tabs

**Pattern:** Implement `CCDConfig<CaseData, State, UserRole>` as `@Component` in `adoptioncase/tab/`

```java
@Component
public class MyTabConfiguration implements CCDConfig<CaseData, State, UserRole> {
    
    @Override
    public void configure(final ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        configBuilder.tab("myTabId", "My Tab Label")
            .forRoles(CASE_WORKER, DISTRICT_JUDGE)
            .showCondition(TabShowCondition.showForState(State.Submitted, State.LaSubmitted))
            .field("field1")
            .field("field2")
            .field("field3", "field2 != null");  // Conditional display
    }
}
```

**Show Conditions:**
```java
.showCondition("applyingWith=\"alone\"")  // Show if condition matches
.label("LabelKey", "condition", "Display Text")  // Dynamic labels
```

---

## 7. Unit Testing

**Pattern:** Use `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks`

```java
public class MyComponentTest {
    
    @Mock
    private DependencyA dependencyA;
    
    @Mock
    private DependencyB dependencyB;
    
    @InjectMocks
    private MyComponent myComponent;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testMyComponentLogic() throws Exception {
        // Setup
        when(dependencyA.getValue()).thenReturn("expected");
        CaseData caseData = TestDataHelper.caseData();
        
        // Execute
        MyResult result = myComponent.process(caseData);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("processed");
        
        // Verify mocks were called correctly
        verify(dependencyA, times(1)).getValue();
        verify(dependencyB).doSomething(any());
    }
    
    @Test
    void testErrorScenario() {
        when(dependencyA.getValue()).thenThrow(new RuntimeException("Error"));
        
        assertThatThrownBy(() -> myComponent.process(null))
            .isInstanceOf(RuntimeException.class);
    }
}
```

---

## 8. Integration Testing

**Pattern:** Extend `FunctionalTest` for CCD API integration tests

```java
@Slf4j
public class MyIntegrationTest extends FunctionalTest {
    
    @Test
    void testCreateCaseAndTriggerEvent() throws IOException {
        // Create case
        CaseDetails caseDetails = createCaseInCcd();
        Long caseId = caseDetails.getId();
        
        // Trigger callback
        Map<String, Object> caseData = getCaseData(caseDetails);
        Response response = triggerCallback(
            caseData,
            "my-event",
            "/callback/my-event"
        );
        
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(200);
        CaseData resultData = response.body().as(CaseData.class);
        assertThat(resultData.getField()).isEqualTo("expected");
    }
}
```

### Test Data Helpers
```java
// Use TestDataHelper for test data
CaseData caseData = TestDataHelper.caseData();
ListValue<AdoptionDocument> document = TestDataHelper.documentWithType(DocumentType.APPLICATION);
Map<String, Object> templateVars = TestDataHelper.getMainTemplateVars(caseData);
```

---

## 9. Using LaunchDarkly Features

**Pattern:** Inject `LaunchDarklyClient` service to check feature flags

```java
@Service
public class MyService {
    
    @Autowired
    private LaunchDarklyClient launchDarklyClient;
    
    public void myMethod(CaseData caseData) {
        if (launchDarklyClient.isFeatureEnabled("my-feature-flag")) {
            // Execute new feature code
            executeNewFeature(caseData);
        } else {
            // Fallback to existing code
            executeExistingFeature(caseData);
        }
    }
}
```

**Configuration:**
```properties
launchdarkly.sdk-key=your-sdk-key
launchdarkly.offline-mode=false
```

---

## 10. Document Generation

**Pattern:** Implement task that calls `CaseDataDocumentService.renderDocumentAndUpdateCaseData()`

```java
@Component
@Slf4j
public class MyDocumentGenerationTask implements CaseTask {
    
    @Autowired
    private CaseDataDocumentService caseDataDocumentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public CaseDetails<CaseData, State> apply(
        final CaseDetails<CaseData, State> caseDetails) {
        
        final CaseData caseData = caseDetails.getData();
        final Long caseId = caseDetails.getId();
        
        log.info("Generating document for CaseID: {}", caseId);
        
        // Prepare template content
        @SuppressWarnings("unchecked")
        Map<String, Object> templateContent = 
            objectMapper.convertValue(caseData, Map.class);
        
        // Select document type based on language preference
        LanguagePreference language = caseData.getApplicant1()
            .getLanguagePreference();
        DocumentType documentType = language.equals(LanguagePreference.ENGLISH)
            ? DocumentType.MY_DOCUMENT_EN
            : DocumentType.MY_DOCUMENT_CY;
        
        // Generate document (async)
        CompletableFuture<Void> generation = CompletableFuture.runAsync(() ->
            caseDataDocumentService.renderDocumentAndUpdateCaseData(
                caseData,
                documentType,
                templateContent,
                caseId,
                "MY_DOCUMENT_TYPE",
                language,
                formatDocumentName(caseId, "MyDocument", LocalDateTime.now())
            )
        );
        
        // Wait for completion
        CompletableFuture.allOf(generation).join();
        
        return caseDetails;
    }
}
```

---

## Common Workflows

### Workflow: Case Submission (Citizen)
1. Event: `CitizenSubmitApplication` (aboutToSubmit callback)
   - Validate case data
   - Return errors if invalid
   - Return updated state
2. Task Pipeline (SubmissionService.submitApplication):
   - `SetStateAfterSubmission` - Determine next state
   - `SetDateSubmitted` - Record submission date
   - `GenerateApplicationSummaryDocument` - Create PDF
3. Event: `CitizenAddPayment` (submittedCallback)
   - Publish `ApplicationSubmitNotificationEvent`
4. Handler: `ApplicationSubmittedNotificationEventHandler`
   - Send emails to applicants, court, local authority

### Workflow: State Transitions
Draft → AwaitingPayment → Submitted → LaSubmitted → [Case Processing]

Use SetState tasks to manage transitions:
```java
CaseTaskRunner.caseTasks(
    setStateAfterSubmission,  // Determines state based on payment
    setDateSubmitted,
    generateDocuments
).run(caseDetails);
```

### Workflow: Multi-Language Support
1. Check `applicant.getLanguagePreference()`
2. Select document type: `DocumentType.MY_DOCUMENT_EN` or `MY_DOCUMENT_CY`
3. Template rendering handles Welsh/English content

---

## Do's & Don'ts

### ✅ DO
- Create tasks as `@Component` implementing `CaseTask`
- Use `CaseTaskRunner.caseTasks()` to compose task pipelines
- Publish events in `submittedCallback`, not `aboutToSubmit`
- Validate in `aboutToSubmit`, perform side effects in `submitted`
- Log with case ID: `log.info("Event {} for CaseID: {}", eventId, caseId)`
- Use dependency injection (`@Autowired`)
- Catch checked exceptions from notification/document services
- Use `@Builder` for records and POJOs

### ❌ DON'T
- Modify case data in async handlers (use tasks instead)
- Publish events in `aboutToSubmit` (must return response)
- Execute long-running operations in callbacks (use tasks/handlers)
- Hardcode case references or state strings (use State enum)
- Mix business logic in event configuration
- Forget to add `@Slf4j` to components
- Use `new` for Spring-managed dependencies

---

## Common Patterns Summary

| Pattern | Location | Key Interface | Key Method |
|---------|----------|---------------|------------|
| CCD Event | adoptioncase/event/ | CCDConfig | configure() |
| Task | */task/ | CaseTask | apply() |
| Service | */service/ | (any) | Methods using CaseTaskRunner |
| Event Handler | notification/handlers/ | (any) | @EventListener methods |
| Notification | notification/ | ApplicantNotification | send*() methods |
| Tab Config | adoptioncase/tab/ | CCDConfig | configure() |
| Scheduled Task | adoptioncase/schedule/ | Runnable | run() |

---

## References
- See [ARCHITECTURE.md](./ARCHITECTURE.md) for detailed examples with full code listings
- Review existing events in `adoptioncase/event/` and `adoptioncase/caseworker/event/`
- Review existing tasks in `common/service/task/`
- Check test examples in `src/test/java/` for testing patterns
