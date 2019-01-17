package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.member_Resources.ResponseMember;
import de.adesso.gitstalker.resources.MemberResources;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MemberProcessorTest {

    private MemberProcessor memberProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private ProcessingRepository processingRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.MEMBER, 1);
    private ResponseMember responseMember;
    private MemberResources memberResources;
    private OrganizationWrapper organizationWrapper;

    @Before
    public void setUp() throws Exception {
        this.memberResources = new MemberResources();
        this.responseMember = new ObjectMapper().readValue(this.memberResources.getExpectedQueryJSONResponse(), ResponseMember.class);
        this.requestRepository = mock(RequestRepository.class);
        this.organizationRepository = mock(OrganizationRepository.class);
        this.processingRepository = mock(ProcessingRepository.class);
        this.organizationWrapper = new OrganizationWrapper("adessoAG");
        when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);
        this.memberProcessor = new MemberProcessor(this.requestRepository, this.organizationRepository, processingRepository);

    }

    @Test
    public void checkIfQueryResponseIsProcessedCorrectly() {
        //Given
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 0, 01);
        MemberProcessor memberProcessor = spy(this.memberProcessor);
        when(memberProcessor.configureCalendarToFitCrawlingPeriod()).thenReturn(calendar);

        //When
        memberProcessor.processQueryResponse(this.responseMember.getData().getNode());

        //Then
        assertEquals(1, memberProcessor.getMembers().size());
        assertEquals(4, memberProcessor.getCommittedRepos().size());
        assertEquals(5, memberProcessor.getMembers().get("MDQ6VXNlcjkxOTkw").getPreviousCommitsWithLink().size());
        assertEquals(11, memberProcessor.getMembers().get("MDQ6VXNlcjkxOTkw").getPreviousIssuesWithLink().size());
        assertEquals(5, memberProcessor.getMembers().get("MDQ6VXNlcjkxOTkw").getPreviousPullRequestsWithLink().size());

        assertEquals("Matthias Balke", memberProcessor.getMembers().get("MDQ6VXNlcjkxOTkw").getName());
        assertEquals("https://avatars0.githubusercontent.com/u/91990?v=4", memberProcessor.getMembers().get("MDQ6VXNlcjkxOTkw").getAvatarURL());
    }

    @Test
    public void checkIfInternalOrganizationCommitsAreProcessedCorrectlyAndMembersAreAssignedWhenQueryFinished() {
        //TODO: Test fertig schreiben. ChartJSData muss gespyt werden. Umschreiben dass Calendar gemockt werden kann.
        //Given
        this.memberProcessor.setRequestQuery(testQuery);

        OrganizationDetail organizationDetail = new OrganizationDetail("adessoAG", "testDescription", "testURL", "testURL", "testLocation", "testAvatarURL", 10, 10, 10);
        this.organizationWrapper.setOrganizationDetail(organizationDetail);
        this.memberProcessor.setOrganization(this.organizationWrapper);

        ArrayList<Query> queries = new ArrayList<>();
        queries.add(testQuery);
        when(this.requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.MEMBER, "adessoAG")).thenReturn(queries);

        HashMap<String, Member> memberHashMap = new HashMap<>();
        memberHashMap.put("memberTestKey", new Member()
                .setName("memberName"));
        this.memberProcessor.setMembers(memberHashMap);

        HashMap<String, Repository> repositoryHashMap = new HashMap<>();
        repositoryHashMap.put("repositoryTestKey", new Repository()
                .setName("repositoryTestName")
                .setUrl("testURL")
                .setDescription("testDescription")
                .setProgrammingLanguage("Java")
                .setLicense("testLicense")
                .setForks(5)
                .setStars(10));
        repositoryHashMap.put("repositoryTestKey2", new Repository()
                .setName("repositoryTestName")
                .setUrl("testURL")
                .setDescription("testDescription")
                .setProgrammingLanguage("Java")
                .setLicense("testLicense")
                .setForks(5)
                .setStars(10));
        this.memberProcessor.getOrganization().setRepositories(repositoryHashMap);

        HashMap<String, ArrayList<Calendar>> committedRepoHashMap = new HashMap<>();
        ArrayList<Calendar> calendars = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 0, 01);
        calendars.add(calendar);
        committedRepoHashMap.put("repositoryTestKey", calendars);

        //When
        this.memberProcessor.calculatesInternalOrganizationCommits();

        //Then
        assertTrue(organizationWrapper.getFinishedRequests().contains(RequestType.MEMBER));
        assertEquals(memberHashMap, organizationWrapper.getMembers());
    }

    @Test
    public void checkIfRequestRepositoryIsAssignedCorrectly() {
        assertSame(this.requestRepository, memberProcessor.getRequestRepository());
    }

    @Test
    public void checkIfOrganizationRepositoryIsAssignedCorrectly() {
        assertSame(this.organizationRepository, memberProcessor.getOrganizationRepository());
    }
}