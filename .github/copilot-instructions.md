# Copilot Instructions — adoption-cos-api

## Build, Test, and Lint Commands

- **Prerequisite:** JDK 21 (set `JAVA_HOME` to `/usr/lib/jvm/java-21-openjdk-<arch>`)
- **Build:** `./gradlew build`
- **Unit tests:** `./gradlew test`
- **Single test:** `./gradlew test --tests 'uk.gov.hmcts.reform.adoption.<ClassName>'`
- **Lint / static analysis:** `./gradlew check` (runs Checkstyle + PMD)
- **Integration tests:** `./gradlew integration`
- **Contract (Pact) tests:** `./gradlew contract`
- **Coverage report:** `./gradlew jacocoTestReport` → `build/reports/`

> **Note:** The README references JDK 17 — this is outdated. JDK 21 is required.

### CCD Config Generation

```bash
./gradlew generateCCDConfig   # JSON definitions → build/definitions/
./gradlew buildCCDXlsx        # Excel spreadsheet (depends on generateCCDConfig)
./gradlew generateTypeScript  # TypeScript enums/constants consumed by adoption-web
```

Run these after changing any CCD model class, event, tab, state, or access profile.

### Local Stack

```bash
./gradlew bootWithCcd   # Starts CCD + XUI locally on http://localhost:3000
```

Requires F5 VPN and `az acr login --name hmctspublic`. AAT IDAM/services are used for auth.

---

## Architecture

This is a **Spring Boot CCD callback API** for the HMCTS Adoption service. It sits between CCD and upstream services — it does not serve a UI directly.

```
adoption-web / XUI
       │
       ▼
     CCD  ←──── adoption-cos-api  (case type: A58, jurisdiction: ADOPTION)
                      │
          ┌───────────┼───────────────┐
          ▼           ▼               ▼
        IDAM    case-document-am   payment-api
                  (doc store)
                                  GOV.UK Notify
                                  (via SendGrid)
```

**Case type:** `A58` | **Jurisdiction:** `ADOPTION`

All external service URLs default to their AAT (`*.aat.platform.hmcts.net`) equivalents, overridable via environment variables (see `application.yaml`).

---

## Key Conventions

### CCDConfig / Event Pattern

Every CCD event is a `@Component` implementing `CCDConfig<CaseData, State, UserRole>`. The `configure()` method registers the event with CCD, wires pages, and attaches callbacks:

```java
@Component
public class CaseworkerFoo implements CCDConfig<CaseData, State, UserRole> {
    public static final String CASEWORKER_FOO = "caseworker-foo";

    private final CcdPageConfiguration fooPage = new FooPage();

    @Override
    public void configure(ConfigBuilder<CaseData, State, UserRole> configBuilder) {
        var pageBuilder = new PageBuilder(configBuilder
            .event(CASEWORKER_FOO)
            .forStates(State.Submitted)
            .name("Foo event")
            .aboutToStartCallback(this::aboutToStart)
            .aboutToSubmitCallback(this::aboutToSubmit)
            .grant(Permissions.CREATE_READ_UPDATE, UserRole.CASE_WORKER));
        fooPage.addTo(pageBuilder);
    }
}
```

Available callbacks: `aboutToStartCallback`, `aboutToSubmitCallback`, `submittedCallback`.

### CcdPageConfiguration / Page Pattern

Each CCD wizard page is a separate class in a `page/` sub-package alongside its event. Pages implement `CcdPageConfiguration`:

```java
public class FooPage implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder.page("fooPage")
            .pageLabel("Foo")
            .complex(CaseData::getFooData)
                .mandatory(FooData::getField1)
                .optional(FooData::getField2)
            .done();
    }
}
```

### CaseTask Pattern

`CaseTask` is a `Function<CaseDetails<CaseData, State>, CaseDetails<CaseData, State>>`. Tasks are `@Component` beans composed and run via `CaseTaskRunner`:

```java
// Definition
@Component
public class MyTask implements CaseTask {
    @Override
    public CaseDetails<CaseData, State> apply(CaseDetails<CaseData, State> details) {
        details.getData().setSomething("value");
        return details;
    }
}

// Usage in an event callback
public AboutToStartOrSubmitResponse<CaseData, State> aboutToSubmit(...) {
    var result = CaseTaskRunner.caseTasks(taskA, taskB, taskC).run(details);
    return AboutToStartOrSubmitResponse.<CaseData, State>builder()
        .data(result.getData())
        .build();
}
```

Tasks under `adoptioncase/task/` and `common/service/task/` handle state transitions, date setting, and document generation. Tasks under `document/task/` handle document creation/removal.

### Notification Pattern

Notifications use Spring's application event system. An event is published after a CCD callback completes, and a handler in `notification/handlers/` picks it up:

```java
// Event record (in service/event/)
public record ApplicationSubmitNotificationEvent(CaseDetails<CaseData, State> caseData) {}

// Handler
@Component
public class ApplicationSubmittedNotificationEventHandler {
    @EventListener
    public void sendNotification(ApplicationSubmitNotificationEvent event) {
        sendNotificationService.sendNotifications(event.caseData());
    }
}
```

### State Machine

States are defined as a `@CCD`-annotated enum (`adoptioncase/model/State.java`). Each state carries an access class controlling which roles can read/write it:

| State | Description |
|---|---|
| `Draft` | Citizen is completing the form |
| `AwaitingPayment` | Application submitted, payment pending |
| `Submitted` | Awaiting Local Authority input |
| `LaSubmitted` | Local Authority has submitted |

State permissions are granted in `Adoption.configure()`.

### Source Sets

| Source set | Task | Purpose |
|---|---|---|
| `src/main` | — | Production code |
| `src/test` | `./gradlew test` | Unit tests |
| `src/integrationTest` | `./gradlew integration` | Integration tests |
| `src/contractTest` | `./gradlew contract` | Pact consumer tests |
| `src/functionalTest` | `./gradlew functional` | Functional/E2E tests |
| `src/cftlib` | `bootWithCcd` | Local CCD stack demo events |

### Test Conventions

- JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`) for unit tests.
- `uk.gov.hmcts.reform.adoption.testutil.TestDataHelper` — factory methods for `CaseData`, `Applicant`, `ListValue<AdoptionDocument>`, etc.
- `uk.gov.hmcts.reform.adoption.testutil.TestConstants` — shared constants (emails, names, IDs).
- `BaseTest` — abstract base for tests that need the Spring context (loads `Adoption.class` + `application-contract.properties`).
- `integrationTest` hits real AAT endpoints; run locally only when connected to F5 VPN.

### Feature Flags (LaunchDarkly)

```java
// With a known user context
launchDarklyClient.isFeatureEnabled("my-flag", ldUser);

// Using the default ADOPTION_COS_USER
launchDarklyClient.isFeatureEnabled("my-flag");
```

The `LaunchDirectlyIntValidationController` exposes a health-check endpoint for flag connectivity.

### Contract Tests (Pact)

- Consumer: `adoption_cos_api`
- Providers tested: `idamApi_oidc`, `case-document-am-api`
- ⚠️ `build.gradle` has `pacticipant = 'sscs_tribunalsCaseApi'` — this is a copy-paste error and needs correcting before publishing pacts to the broker.
- Run tests: `./gradlew contract`; publish: `./gradlew runAndPublishConsumerPactTests` (requires `PACT_BROKER_FULL_URL`).
