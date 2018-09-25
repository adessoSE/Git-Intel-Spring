package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.memberID_Resources.PageInfo;
import de.adesso.gitstalker.core.resources.memberID_Resources.ResponseMemberID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MemberIDProcessorTest {

    private MemberIDProcessor memberIDProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.MEMBER_ID, 1);
    private ResponseMemberID responseMemberID;
    private String responseMemberIDRequest = "{\n" +
            "  \"data\": {\n" +
            "    \"organization\": {\n" +
            "      \"members\": {\n" +
            "        \"pageInfo\": {\n" +
            "          \"hasNextPage\": true,\n" +
            "          \"endCursor\": \"Y3Vyc29yOnYyOpHOAlyCJg==\"\n" +
            "        },\n" +
            "        \"nodes\": [\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjkxOTkw\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI3NTI2OA==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM0OTE4OA==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjQyMDc0MQ==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjYxNzg0Nw==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjY4MDc0MA==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjY4NjY5Mg==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjcyMDE5OQ==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjc3ODUzNg==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjk1MjczMA==\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEyMDA0MjM=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEyNDY1NjY=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEzNjM1Nzg=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE0MDEwMzg=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE0MDE4Mjk=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE2NjY1NDk=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE2Njg4NTU=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIyOTczNzI=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIzOTIyMTc=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjMxNTUxNzA=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjMzNTE4Njg=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjQ2OTAxNTA=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjUwNTcwMTI=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjUxNTQ3NTA=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjUzOTA4OTk=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjU3MjkzNzY=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjU3OTY5NjI=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjU4Njk3NzA=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjYwODkxMjI=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjY1OTcwMDg=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjY3Mjg2MDE=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjc1OTAxNjM=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjc4NDU5MDM=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjgzOTQ5Mjk=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjg1NjgwMzU=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjkwNzY5MDE=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjkxMDk4MDM=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjkyMDkzNDQ=\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEwMjE4OTQ0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjExMDEwMzEw\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEyMjAxODM1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEyNDU4NTk5\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEzMTM0Nzgw\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEzMzM4MzQz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjEzODE5NDgw\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE0ODM0NDk1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE1NjA5NDkz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE1NjcwMDIz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE1NjcyMjA4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE2MDY5NzUx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE2MzUwMjYz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE2ODE4MTcx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE3MjAyNzMz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE4MjYzNzc0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE5ODYxOTk4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjE5OTk2ODU4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIwMDc1MDQ0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIwMzk3OTcx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIwNDgzNDAx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIzMDg2OTgy\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIzMjc1MjIy\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIzNDI0NTM4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjIzNTUyNDAx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI0NTIwOTI3\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI0OTkxMzA5\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI1MzAwODIx\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI1MzQzMjE1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI2MDAyNDY3\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI4MzgzNTMw\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjI5MjAxNTUy\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjMwODI4MjI2\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjMyNTYyNDI2\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjMzMjUyMzA1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM0MDI4MDY5\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM0NzA4NDYy\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM1MDIzMDgz\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM5MTkwNjY4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM5MTkyMjg2\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"id\": \"MDQ6VXNlcjM5NjE3MDYy\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    },\n" +
            "    \"rateLimit\": {\n" +
            "      \"cost\": 1,\n" +
            "      \"remaining\": 4999,\n" +
            "      \"resetAt\": \"2018-09-25T09:31:07Z\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.responseMemberID = objectMapper.readValue(responseMemberIDRequest, ResponseMemberID.class);
        this.requestRepository = Mockito.mock(RequestRepository.class);
        this.organizationRepository = Mockito.mock(OrganizationRepository.class);
        this.memberIDProcessor = new MemberIDProcessor();
    }

    @Test
    public void checkIfMemberIDsAreProcessedCorrectly() {
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        memberIDProcessor.processQueryResponse(this.responseMemberID.getData().getOrganization().getMembers());

        assertEquals(79, memberIDProcessor.getMemberIDs().size());
        assertEquals("MDQ6VXNlcjkxOTkw", memberIDProcessor.getMemberIDs().get(0));
    }

    @Test
    public void checkIfFollowingRequestsAreGenerated() {
        //Given
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        memberIDProcessor.processQueryResponse(this.responseMemberID.getData().getOrganization().getMembers());

        //When
        memberIDProcessor.generateNextRequestsBasedOnMemberIDs(memberIDProcessor.getMemberIDs());

        //Then (Total processedMemberIDs: 79)
        verify(requestRepository, times(158)).save(Mockito.any(Query.class));
    }

    @Test
    public void checkIfRequestForRemainingInformationWillBeGenerated() {
        //Given
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(true);

        //When
        memberIDProcessor.processRequestForRemainingInformation(pageInfo, "adessoAG");

        //Then
        verify(requestRepository, times(1)).save(Mockito.any(Query.class));
    }

    @Test
    public void checkIfAfterCollectingAllInformationAreSavedAndRequestsAreGenerated() {
        //Given
        OrganizationWrapper organizationWrapper = new OrganizationWrapper("adessoAG");
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);

        ArrayList<String> memberIDs = new ArrayList<>();
        memberIDs.add("testMemberID1");
        memberIDs.add("testMemberID2");
        memberIDs.add("testMemberID3");
        memberIDProcessor.setMemberIDs(memberIDs);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);


        //When
        memberIDProcessor.processRequestForRemainingInformation(pageInfo, "adessoAG");

        //Then
        verify(requestRepository, times(6)).save(Mockito.any(Query.class));
        assertEquals(memberIDs, memberIDProcessor.getOrganization().getMemberIDs());
    }

    @Test
    public void checkIfRequestQueryIsAssignedCorrectly() {
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(testQuery, memberIDProcessor.getRequestQuery());
    }

    @Test
    public void checkIfRequestRepositoryIsAssignedCorrectly() {
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.requestRepository, memberIDProcessor.getRequestRepository());
    }

    @Test
    public void checkIfOrganizationRepositoryIsAssignedCorrectly() {
        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.organizationRepository, memberIDProcessor.getOrganizationRepository());
    }

    @Test
    public void checkIfOrganizationIsAssignedCorrectly() {
        OrganizationWrapper organizationWrapper = new OrganizationWrapper("adessoAG");
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);

        memberIDProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);

        assertSame(organizationWrapper, memberIDProcessor.getOrganization());
    }
}