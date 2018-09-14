package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RequestManagerTest {

    @Test
    public void checkIfOrganizationValidationRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.ORGANIZATION_VALIDATION);
        assertEquals(RequestType.ORGANIZATION_VALIDATION, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfOrganizationDetailRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.ORGANIZATION_DETAIL);
        assertEquals(RequestType.ORGANIZATION_DETAIL, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfMemberIDRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER_ID);
        assertEquals(RequestType.MEMBER_ID, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfMemberPRRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER_PR);
        assertEquals(RequestType.MEMBER_PR, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfMemberRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testMemberID", RequestType.MEMBER);
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER);
        assertEquals(RequestType.MEMBER, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfRepositoryRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.REPOSITORY);
        assertEquals(RequestType.REPOSITORY, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfTeamRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.TEAM);
        assertEquals(RequestType.TEAM, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfExternalRepoRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager(Arrays.asList("testRepoID"), "adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.EXTERNAL_REPO);
        assertEquals(RequestType.EXTERNAL_REPO, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfCreatedRepoByMemberRequestIsGeneratedCorrectly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testMemberID", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS);
        assertEquals(RequestType.CREATED_REPOS_BY_MEMBERS, generatedTeamQuery.getQueryRequestType());
    }

    @Test
    public void checkIfAllRequestAreGeneratedAfterValidation(){
        RequestManager requestManager = new RequestManager("adessoAG");
        ArrayList<Query> generatedRequestsAfterValidation = requestManager.generateAllRequests();
        assertEquals(4,generatedRequestsAfterValidation.size());
    }

    @Test
    public void checkIfInputIsFormattedCorrectly(){
        RequestManager requestManager = new RequestManager("adessoAG");
        String formattedInput = requestManager.formatInput("adesso AG");
        assertEquals("adessoag", formattedInput);
    }
}