package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.memberID_Resources.PageInfo;
import de.adesso.gitstalker.core.resources.memberID_Resources.ResponseMemberID;
import de.adesso.gitstalker.resources.MemberIDResources;
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
    private MemberIDResources memberIDResources;

    @Before
    public void setUp() throws Exception {
        this.memberIDResources = new MemberIDResources();
        this.responseMemberID = new ObjectMapper().readValue(this.memberIDResources.getExpectedQueryJSONResponse(), ResponseMemberID.class);
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