package uk.gov.hmcts.cp.cdk.domain;

import java.util.List;

public record Document(String documentTypeId,
                       String documentTypeDescription,
                       List<Material> materials) {

}

