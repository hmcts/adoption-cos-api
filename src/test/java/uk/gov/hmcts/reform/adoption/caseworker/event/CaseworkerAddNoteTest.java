package uk.gov.hmcts.reform.adoption.caseworker.event;


//@ExtendWith(MockitoExtension.class)
class CaseworkerAddNoteTest {

    /*@Mock
    private IdamService idamService;*/

    /*@Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Clock clock;

    @InjectMocks
    private CaseworkerCaseNote caseworkerAddNote;*/

    /*@Test
    void shouldAddConfigurationToConfigBuilder() throws Exception {
        final ConfigBuilderImpl<CaseData, State, UserRole> configBuilder = createCaseDataConfigBuilder();

        caseworkerAddNote.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(CASEWORKER_ADD_CASE_NOTE);
    }*/

    /*@Test
    public void shouldSuccessfullyAddCaseNoteToCaseDataWhenThereAreNoExistingCaseNotes() {
        final CaseData caseData = caseData();
        CaseNote caseNote = new CaseNote();
        caseNote.setNote("TEST NOTE");
        caseNote.setSubject("TEST SUBJECT");
        ListValue caseNoteListValue = new ListValue();
        caseNoteListValue.setId(LocalDate.now().toString());
        caseNoteListValue.setValue(caseNote);
        List<ListValue<CaseNote>> caseNoteList = new ArrayList<>();
        caseNoteList.add(caseNoteListValue);
        caseData.setCaseNote(caseNoteList);

        final CaseDetails<CaseData, State> updatedCaseDetails = new CaseDetails<>();
        updatedCaseDetails.setData(caseData);
        updatedCaseDetails.setId(TEST_CASE_ID);
        updatedCaseDetails.setCreatedDate(LOCAL_DATE_TIME);

        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);



       *//*AboutToStartOrSubmitResponse<CaseData, State> response =
            caseworkerAddNote.configure(createCaseDataConfigBuilder());

        assertThat(response.getData().getNotes())
            .extracting("id", "value.author", "value.note")
            .contains(tuple("1", "testFname testSname", "This is a test note"));

        assertThat(response.getData().getNotes())
            .extracting("value.date", LocalDate.class)
            .allMatch(localDate -> localDate.isEqual(expectedDate));

        assertThat(response.getData().getNote()).isNull();*//*

        //verify(httpServletRequest).getHeader(AUTHORIZATION);
        //verify(idamService).retrieveUser(TEST_AUTHORIZATION_TOKEN);
        //verifyNoMoreInteractions(httpServletRequest, idamService);
    }*/

    /*@Test
    public void shouldSuccessfullyAddCaseNoteToStartOfCaseNotesListWhenThereIsExistingCaseNote() {
        final CaseData caseData = caseData();
        caseData.setNote("This is a test note 2");

        var caseNoteAddedDate = LocalDate.of(2021, 1, 1);

        var notes = new ArrayList<ListValue<CaseNote>>();
        notes.add(ListValue
            .<CaseNote>builder()
            .id("1")
            .value(new CaseNote("TestFirstName TestSurname", caseNoteAddedDate, "This is a test note 1"))
            .build());

        caseData.setNotes(notes);

        final CaseDetails<CaseData, State> updatedCaseDetails = new CaseDetails<>();
        updatedCaseDetails.setData(caseData);
        updatedCaseDetails.setId(TEST_CASE_ID);
        updatedCaseDetails.setCreatedDate(LOCAL_DATE_TIME);

        final var instant = Instant.now();
        final var zoneId = ZoneId.systemDefault();
        final var expectedDate = LocalDate.ofInstant(instant, zoneId);

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(zoneId);

        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn(TEST_AUTHORIZATION_TOKEN);

        when(idamService.retrieveUser(TEST_AUTHORIZATION_TOKEN)).thenReturn(getCaseworkerUser());

        AboutToStartOrSubmitResponse<CaseData, State> response =
            caseworkerAddNote.aboutToSubmit(updatedCaseDetails, CaseDetails.<CaseData, State>builder().build());

        assertThat(response.getData().getNotes())
            .extracting("id", "value.author", "value.note")
            .containsExactly(
                tuple("1", "testFname testSname", "This is a test note 2"),
                tuple("2", "TestFirstName TestSurname", "This is a test note 1")

            );

        assertThat(response.getData().getNotes())
            .extracting("value.date", LocalDate.class)
            .containsExactlyInAnyOrder(expectedDate, caseNoteAddedDate);

        assertThat(response.getData().getNote()).isNull();

        verify(httpServletRequest).getHeader(AUTHORIZATION);
        verify(idamService).retrieveUser(TEST_AUTHORIZATION_TOKEN);
        verifyNoMoreInteractions(httpServletRequest, idamService);
    }

    private User getCaseworkerUser() {
        UserDetails userDetails = UserDetails
            .builder()
            .forename("testFname")
            .surname("testSname")
            .build();

        return new User(TEST_AUTHORIZATION_TOKEN, userDetails);
    }*/

    /*public static ConfigBuilderImpl<CaseData, State, UserRole> createCaseDataConfigBuilder() {
        return new ConfigBuilderImpl<>(new ResolvedCCDConfig<>(
            CaseData.class,
            State.class,
            UserRole.class,
            new HashMap<>(),
            ImmutableSet.copyOf(State.class.getEnumConstants())));
    }

    public static <T, S, R extends HasRole> Map<String, Event<T, R, S>> getEventsFrom(
        final ConfigBuilderImpl<T, S, R> configBuilder) {

        return (Map<String, Event<T, R, S>>) findMethod(ConfigBuilderImpl.class, "getEvents")
            .map(method -> {
                try {
                    method.setAccessible(true);
                    return method.invoke(configBuilder);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError("Unable to invoke ConfigBuilderImpl.class method getEvents", e);
                }
            })
            .orElseThrow(() -> new AssertionError("Unable to find ConfigBuilderImpl.class method getEvents"));
    }*/

}
