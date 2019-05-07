package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrganizationValidationRequestTest {

    private OrganizationValidationRequest organizationValidationRequest;
    private String expectedGeneratedQueryContent = "query {\n" +
            "organization(login:\"adessoAG\") {\n" +
            "name\n" +
            "membersWithRole(first: 1) {\n" +
            "totalCount\n" +
            "}\n" +
            "repositories(first: 1) {\n" +
            "totalCount\n" +
            "}\n" +
            "teams(first: 1) {\n" +
            "totalCount\n" +
            "}\n" +
            "}\n" +
            "rateLimit {\n" +
            "cost\n" +
            "remaining\n" +
            "resetAt\n" +
            "}\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        this.organizationValidationRequest = new OrganizationValidationRequest("adessoAG");
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.organizationValidationRequest.generateQuery();
        assertEquals("adessoAG", query.getOrganizationName());
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.organizationValidationRequest.generateQuery();
        assertEquals(RequestType.ORGANIZATION_VALIDATION, query.getQueryRequestType());
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.organizationValidationRequest.generateQuery();
        assertEquals(this.expectedGeneratedQueryContent, query.getQueryContent());
    }
}