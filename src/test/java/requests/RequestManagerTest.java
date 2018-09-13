package requests;

import enums.RequestType;
import objects.Query;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class RequestManagerTest {

    @Test
    public void checkIfOrganizationValidationRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.ORGANIZATION_VALIDATION);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.ORGANIZATION_VALIDATION);
    }

    @Test
    public void checkIfOrganizationDetailRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.ORGANIZATION_DETAIL);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.ORGANIZATION_DETAIL);
    }

    @Test
    public void checkIfMemberIDRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER_ID);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.MEMBER_ID);
    }

    @Test
    public void checkIfMemberPRRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER_PR);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.MEMBER_PR);
    }

    @Test
    public void checkIfMemberRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testMemberID", RequestType.MEMBER);
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.MEMBER);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.MEMBER);
    }

    @Test
    public void checkIfRepositoryRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.REPOSITORY);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.REPOSITORY);
    }

    @Test
    public void checkIfTeamRequestIsGenereatedCorretly() {
    RequestManager requestManager = new RequestManager("adessoAG", "testEndCursor");
    Query generatedTeamQuery = requestManager.generateRequest(RequestType.TEAM);
    assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.TEAM);
    }

    @Test
    public void checkIfExternalRepoRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager(Arrays.asList("testRepoID"), "adessoAG");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.EXTERNAL_REPO);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.EXTERNAL_REPO);
    }

    @Test
    public void checkIfCreatedRepoByMemberRequestIsGenereatedCorretly() {
        RequestManager requestManager = new RequestManager("adessoAG", "testMemberID", "testEndCursor");
        Query generatedTeamQuery = requestManager.generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS);
        assertEquals(generatedTeamQuery.getQueryRequestType(), RequestType.CREATED_REPOS_BY_MEMBERS);
    }
}