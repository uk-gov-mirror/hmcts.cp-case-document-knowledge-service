package uk.gov.hmcts.cp.cdk.domain;

import java.time.ZonedDateTime;

public record Material (String id,
                        ZonedDateTime uploadDateTime
){
}
