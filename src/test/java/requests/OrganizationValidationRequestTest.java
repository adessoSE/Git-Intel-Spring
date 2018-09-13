package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrganizationValidationRequestTest {

    private OrganizationValidationRequest organizationValidationRequest;
    private String expectedGeneratedQueryContent = "query {\n" +
            "organization(login:\"adessoAG\") {\n" +
            "id\n" +
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
        assertEquals(query.getOrganizationName(), "adessoAG");
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.organizationValidationRequest.generateQuery();
        assertEquals(query.getQueryRequestType(), RequestType.ORGANIZATION_VALIDATION);
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.organizationValidationRequest.generateQuery();
        assertEquals(query.getQuery(), this.expectedGeneratedQueryContent);
    }
}