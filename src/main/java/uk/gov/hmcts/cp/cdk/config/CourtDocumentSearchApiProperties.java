package uk.gov.hmcts.cp.cdk.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "courtdocument.api")
public class CourtDocumentSearchApiProperties {

    private String courtDocumentSearchUrlTemplate;
    private String actionHeader;
    private String userIdHeader;

    public String getCourtDocumentSearchUrlTemplate() {
        return courtDocumentSearchUrlTemplate;
    }

    public void setCourtDocumentSearchUrlTemplate(String courtDocumentSearchUrlTemplate) {
        this.courtDocumentSearchUrlTemplate = courtDocumentSearchUrlTemplate;
    }

    public String getActionHeader() {
        return actionHeader;
    }

    public void setActionHeader(String actionHeader) {
        this.actionHeader = actionHeader;
    }

    public String getUserIdHeader() {
        return userIdHeader;
    }

    public void setUserIdHeader(String userIdHeader) {
        this.userIdHeader = userIdHeader;
    }
}
