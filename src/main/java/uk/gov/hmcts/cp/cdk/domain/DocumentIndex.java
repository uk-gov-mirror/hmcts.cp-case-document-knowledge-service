package uk.gov.hmcts.cp.cdk.domain;

import java.util.List;

public record DocumentIndex (List<String> caseIds,
                             Document document){

}
