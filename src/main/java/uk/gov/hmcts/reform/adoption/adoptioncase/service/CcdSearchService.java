package uk.gov.hmcts.reform.adoption.adoptioncase.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.SearchResult;
import uk.gov.hmcts.reform.idam.client.models.User;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import static uk.gov.hmcts.reform.adoption.adoptioncase.Adoption.CASE_TYPE;

@Service
@Slf4j
public class CcdSearchService {

    public static final String ACCESS_CODE = "data.accessCode";
    public static final String DUE_DATE = "data.dueDate";
    public static final String ISSUE_DATE = "data.issueDate";
    public static final String DATA = "data.%s";
    public static final String STATE = "state";
    public static final String CREATED_DATE = "created_date";
    public static final String AOS_RESPONSE = "data.howToRespondApplication";
    public static final String FINAL_ORDER_ELIGIBLE_FROM_DATE = "data.dateFinalOrderEligibleFrom";
    public static final String FINAL_ORDER_ELIGIBLE_TO_RESPONDENT_DATE = "data.dateFinalOrderEligibleToRespondent";
    public static final String SUBMITTED_DATE = "data.dateSubmitted";

    @Value("${core_case_data.search.page_size}")
    private int pageSize;

    @Autowired
    private CoreCaseDataApi coreCaseDataApi;

    public List<CaseDetails> searchForAllCasesWithQuery(final State state, final BoolQueryBuilder query, User user, String serviceAuth) {

        final List<CaseDetails> allCaseDetails = new ArrayList<>();
        int from = 0;
        int totalResults = pageSize;

        try {
            while (totalResults == pageSize) {
                log.info("Query for sending Emails : " + query.toString());
                final SearchResult searchResult =
                    searchForCasesWithQuery(from, pageSize, query, user, serviceAuth);

                allCaseDetails.addAll(searchResult.getCases());

                from += pageSize;
                totalResults = searchResult.getTotal();
                log.info("Total Results: " + totalResults);
            }
        } catch (final FeignException e) {
            final String message = String.format("Failed to complete search for Cases with state of %s", state);
            log.info(message, e);
            throw new CcdSearchCaseException(message, e);
        }

        return allCaseDetails;
    }

    public SearchResult searchForCasesWithQuery(final int from,
                                                final int size,
                                                final BoolQueryBuilder query,
                                                final User user,
                                                final String serviceAuth) {

        final SearchSourceBuilder sourceBuilder = SearchSourceBuilder
            .searchSource()
            .sort(DUE_DATE, ASC)
            .query(query)
            .from(from)
            .size(size);

        return coreCaseDataApi.searchCases(
            user.getAuthToken(),
            serviceAuth,
            CASE_TYPE,
            sourceBuilder.toString());
    }

    public List<CaseDetails> searchForCasesWithVersionLessThan(int latestVersion, User user, String serviceAuth) {

        final SearchSourceBuilder sourceBuilder = SearchSourceBuilder
            .searchSource()
            .query(
                boolQuery()
                    .must(boolQuery()
                              .mustNot(matchQuery("data.dataVersion", 0))
                    )
                    .must(boolQuery()
                              .should(boolQuery().mustNot(existsQuery("data.dataVersion")))
                              .should(boolQuery().must(rangeQuery("data.dataVersion").lt(latestVersion)))
                    )
            )
            .from(0)
            .size(500);

        return coreCaseDataApi.searchCases(
            user.getAuthToken(),
            serviceAuth,
            CASE_TYPE,
            sourceBuilder.toString()
        ).getCases();
    }

    public List<CaseDetails> searchForCases(
        final List<String> caseReferences,
        final User user,
        final String serviceAuth) {

        final QueryBuilder bulkCaseDetailsExist = termsQuery("reference", caseReferences);
        final SearchSourceBuilder sourceBuilder = SearchSourceBuilder
            .searchSource()
            .query(
                boolQuery()
                    .must(bulkCaseDetailsExist)
            )
            .from(0)
            .size(50);

        return coreCaseDataApi.searchCases(
            user.getAuthToken(),
            serviceAuth,
            CASE_TYPE,
            sourceBuilder.toString()
        ).getCases();
    }
}
