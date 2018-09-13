package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrganizationDetailRequestTest {

    private OrganizationDetailRequest organizationDetailRequest;
    private String expectedGeneratedQueryContent = "query {\n" +
            "organization(login:\"adessoAG\") {\n" +
            "name \n" +
            "location \n" +
            "websiteUrl \n" +
            "url \n" +
            "avatarUrl \n" +
            "description \n" +
            "members(first: 1) {\n" +
            "totalCount\n" +
            "}\n" +
            "teams(first: 1) {\n" +
            "totalCount\n" +
            "}\n" +
            "repositories(first: 1) {\n" +
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
        this.organizationDetailRequest = new OrganizationDetailRequest("adessoAG");
    }

    @Test
    public void checkIfOrganizationNameIsCorrectInGeneratedQuery() {
        Query query = this.organizationDetailRequest.generateQuery();
        assertEquals(query.getOrganizationName(), "adessoAG");
    }

    @Test
    public void checkIfRequestTypeIsCorrectInGeneratedQuery() {
        Query query = this.organizationDetailRequest.generateQuery();
        assertEquals(query.getQueryRequestType(), RequestType.ORGANIZATION_DETAIL);
    }

    @Test
    public void checkIfQueryContentIsGeneratedCorretly() {
        Query query = this.organizationDetailRequest.generateQuery();
        assertEquals(query.getQuery(), this.expectedGeneratedQueryContent);
    }
}