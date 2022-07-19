package uk.gov.hmcts.reform.adoption.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Value;

import java.util.Base64;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Value
public class DocAssemblyRequest {
    private String templateId;

    private String outputType;

    private JsonNode formPayload;

    private String outputFilename;

    private Boolean secureDocStoreEnabled;

    private String caseTypeId;

    private String jurisdictionId;

    public static class DocAssemblyRequestBuilder {
        public DocAssemblyRequestBuilder templateId(String templateId) {
            this.templateId = Base64.getEncoder()
                .encodeToString(templateId.getBytes());
            return this;
        }
    }
}
