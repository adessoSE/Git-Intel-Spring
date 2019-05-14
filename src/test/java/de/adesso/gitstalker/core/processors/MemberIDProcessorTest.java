package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MemberIDProcessorTest {

    private MemberIDProcessor memberIDProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.MEMBER_ID, 1);
    private ResponseMemberID responseMemberID;
    private MemberIDResources memberIDResources;
    private OrganizationWrapper organizationWrapper;

    @Before
    public void setUp() throws Exception {
        this.memberIDResources = new MemberIDResources();
        this.responseMemberID = new ObjectMapper().readValue(this.memberIDResources.getExpectedQueryJSONResponse(), ResponseMemberID.class);
        this.requestRepository = mock(RequestRepository.class);
        this.organizationRepository = mock(OrganizationRepository.class);
        this.processingRepository = mock(ProcessingRepository.class);
        this.organizationWrapper = new OrganizationWrapper("adessoAG");
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);
        this.memberIDProcessor = new MemberIDProcessor(this.requestRepository, this.organizationRepository, processingRepository);
    }
/*
    @Test
    public void checkIfMemberIDsAreProcessedCorrectly() {
        memberIDProcessor.processQueryResponse(this.responseMemberID.getData().getOrganization().getMembers());

        assertEquals(79, memberIDProcessor.getMemberIDs().size());
        assertEquals("MDQ6VXNlcjkxOTkw", memberIDProcessor.getMemberIDs().get(0));
    }

    @Test
    public void checkIfFollowingRequestsAreGenerated() {
        //Given
        memberIDProcessor.setRequestQuery(testQuery);
        memberIDProcessor.processQueryResponse(this.responseMemberID.getData().getOrganization().getMembers());

        //When
        memberIDProcessor.generateNextRequestsBasedOnMemberIDs(memberIDProcessor.getMemberIDs());

        //Then (Total processedMemberIDs: 79)
        verify(requestRepository, times(158)).save(Mockito.any(Query.class));
    }*/

    @Test
    public void checkIfRequestForRemainingInformationWillBeGenerated() {
        //Given
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
        ArrayList<String> memberIDs = new ArrayList<>();
        memberIDs.add("testMemberID1");
        memberIDs.add("testMemberID2");
        memberIDs.add("testMemberID3");
        memberIDProcessor.setMemberIDs(memberIDs);
        memberIDProcessor.setRequestQuery(testQuery);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setHasNextPage(false);

        this.memberIDProcessor.setOrganization(this.organizationWrapper);

        //When
        memberIDProcessor.processRequestForRemainingInformation(pageInfo, "adessoAG");

        //Then
        verify(requestRepository, times(6)).save(Mockito.any(Query.class));
        assertEquals(memberIDs, memberIDProcessor.getOrganization().getMemberIDs());
    }

    @Test
    public void checkIfRequestRepositoryIsAssignedCorrectly() {
        assertSame(this.requestRepository, memberIDProcessor.getRequestRepository());
    }

    @Test
    public void checkIfOrganizationRepositoryIsAssignedCorrectly() {
        assertSame(this.organizationRepository, memberIDProcessor.getOrganizationRepository());
    }
}