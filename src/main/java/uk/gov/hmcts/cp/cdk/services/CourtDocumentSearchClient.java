package uk.gov.hmcts.cp.cdk.services;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.cp.cdk.config.CourtDocumentSearchApiProperties;
import uk.gov.hmcts.cp.cdk.domain.CourtDocumentSearchResponse;
import uk.gov.hmcts.cp.cdk.domain.LatestMaterialInfo;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CourtDocumentSearchClient {

    private final RestTemplate restTemplate;
    private final CourtDocumentSearchApiProperties properties;

    public CourtDocumentSearchClient(RestTemplate restTemplate,
                                     CourtDocumentSearchApiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    /**
     * Fetch latest document info for a given caseId and userId
     */
    public List<LatestMaterialInfo> fetchLatestCourtDocuments(String caseId, String userId) {

        // Build the URL from template in properties
        final String url = properties.getCourtDocumentSearchUrlTemplate()
                .replace("{caseId}", caseId);

        // Prepare headers from properties
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", properties.getActionHeader());
        headers.add(properties.getUserIdHeader(), userId);


        // Build request
        RequestEntity<Void> request = RequestEntity.get(URI.create(url))
                .headers(headers)
                .build();

        // Call Micro service
        ResponseEntity<CourtDocumentSearchResponse> response =
                restTemplate.exchange(request, CourtDocumentSearchResponse.class);

        CourtDocumentSearchResponse body = response.getBody();

        if (body == null || body.documentIndices() == null || body.documentIndices().isEmpty()) {
            return List.of(); // empty list if no data
        }

        // Process each DocumentIndex and extract latest Material
        return body.documentIndices().stream()
                .map(this::mapToLatestMaterialInfo)
                .flatMap(Optional::stream) // remove empty optionals
                .toList();
    }

    private Optional<LatestMaterialInfo> mapToLatestMaterialInfo(CourtDocumentSearchResponse.DocumentIndex index) {
        List<String> caseIds = index.caseIds();
        CourtDocumentSearchResponse.Document document = index.document();

        if (document == null) return Optional.empty();

        String documentTypeId = document.documentTypeId();
        String documentTypeDescription = document.documentTypeDescription();

        // Find latest material
        Optional<CourtDocumentSearchResponse.Material> latestMaterial = document.materials() == null ? Optional.empty() :
                document.materials().stream()
                        .filter(m -> m.uploadDateTime() != null)
                        .max(Comparator.comparing(CourtDocumentSearchResponse.Material::uploadDateTime));

        return latestMaterial.map(material ->
                new LatestMaterialInfo(
                        caseIds,
                        documentTypeId,
                        documentTypeDescription,
                        material.id(),
                        material.uploadDateTime()
                )
        );
    }


}
