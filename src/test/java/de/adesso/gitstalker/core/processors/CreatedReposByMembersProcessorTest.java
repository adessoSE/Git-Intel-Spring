package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationDetail;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Repository;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.createdReposByMembers.*;
import de.adesso.gitstalker.resources.CreatedReposByMemberResources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreatedReposByMembersProcessorTest {

    private CreatedReposByMembersProcessor createdReposByMembersProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.MEMBER, 1);
    private ResponseCreatedReposByMembers responseCreatedReposByMembers;
    private CreatedReposByMemberResources createdReposByMemberResources;

    @Before
    public void setUp() throws Exception {
        this.createdReposByMemberResources = new CreatedReposByMemberResources();
        this.responseCreatedReposByMembers = new ObjectMapper().readValue(this.createdReposByMemberResources.getResponseCreatedReposByMemberRequest(), ResponseCreatedReposByMembers.class);
        this.requestRepository = mock(RequestRepository.class);
        this.organizationRepository = mock(OrganizationRepository.class);
        this.createdReposByMembersProcessor = new CreatedReposByMembersProcessor();
    }

    @Test
    public void checkIfEmptyListContainsMemberID() {
        assertFalse(createdReposByMembersProcessor.checkIfMemberIsAlreadyAssigned("test"));
    }

    @Test
    public void checkIfRepositoryAmountIsProcessedCorrectly() {
        createdReposByMembersProcessor.processQueryResponse(this.responseCreatedReposByMembers.getData());
        HashMap<String, ArrayList<Repository>> createdRepositoriesByMembers = createdReposByMembersProcessor.getCreatedRepositoriesByMembers();

        assertEquals(1, createdRepositoriesByMembers.get("MDQ6VXNlcjkxOTkw").size());
    }

    @Test
    public void checkIfRepositoryIsAssignedCorrectlyToExistingMember(){
        ArrayList<Repository> repositories = new ArrayList<>();
        repositories.add(new Repository(null, null, null, null, null, 0, 0));
        HashMap<String, ArrayList<Repository>> createdRepositoriesByMembers = createdReposByMembersProcessor.getCreatedRepositoriesByMembers();
        createdRepositoriesByMembers.put("MDQ6VXNlcjkxOTkw", repositories);
        createdReposByMembersProcessor.setCreatedRepositoriesByMembers(createdRepositoriesByMembers);

        createdReposByMembersProcessor.processQueryResponse(this.responseCreatedReposByMembers.getData());

        assertEquals(2, createdReposByMembersProcessor.getCreatedRepositoriesByMembers().get("MDQ6VXNlcjkxOTkw").size());
    }

    @Test
    public void checkIfListContainsMemberID() {
        createdReposByMembersProcessor.processQueryResponse(this.responseCreatedReposByMembers.getData());

        assertTrue(createdReposByMembersProcessor.checkIfMemberIsAlreadyAssigned("MDQ6VXNlcjkxOTkw"));
    }

    @Test
    public void checkIfRepositoryIsGeneratedCorrectly() {
        createdReposByMembersProcessor.processQueryResponse(this.responseCreatedReposByMembers.getData());

        Repository expectedRepo = new Repository("magento", "https://github.com/matthiasbalke/magento", "Magento Modules", "", "", 0, 1);
        Repository actualRepo = createdReposByMembersProcessor.getCreatedRepositoriesByMembers().get("MDQ6VXNlcjkxOTkw").get(0);

        assertEquals(expectedRepo.getName(), actualRepo.getName());
        assertEquals(expectedRepo.getDescription(), actualRepo.getDescription());
        assertEquals(expectedRepo.getUrl(), actualRepo.getUrl());
        assertEquals(expectedRepo.getLicense(), actualRepo.getLicense());
        assertEquals(expectedRepo.getProgrammingLanguage(), actualRepo.getProgrammingLanguage());
        assertEquals(expectedRepo.getForks(), actualRepo.getForks());
        assertEquals(expectedRepo.getStars(), actualRepo.getStars());
    }

    @Test
    public void checkIfLicenseIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setLicenseInfo(null);

        assertNotNull(createdReposByMembersProcessor.getLicense(nodesRepositories));
        assertEquals("", createdReposByMembersProcessor.getLicense(nodesRepositories));
    }

    @Test
    public void checkIfLicenseIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setName("MIT");
        nodesRepositories.setLicenseInfo(licenseInfo);

        assertNotNull(createdReposByMembersProcessor.getLicense(nodesRepositories));
        assertEquals("MIT", createdReposByMembersProcessor.getLicense(nodesRepositories));
    }

    @Test
    public void checkIfProgrammingLanguageIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setPrimaryLanguage(null);

        assertNotNull(createdReposByMembersProcessor.getProgrammingLanguage(nodesRepositories));
        assertEquals("", createdReposByMembersProcessor.getProgrammingLanguage(nodesRepositories));
    }

    @Test
    public void checkIfProgrammingLanguageIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        PrimaryLanguage primaryLanguage = new PrimaryLanguage();
        primaryLanguage.setName("Java");
        nodesRepositories.setPrimaryLanguage(primaryLanguage);

        assertNotNull(createdReposByMembersProcessor.getProgrammingLanguage(nodesRepositories));
        assertEquals("Java", createdReposByMembersProcessor.getProgrammingLanguage(nodesRepositories));
    }

    @Test
    public void checkIfDescriptionIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setDescription(null);

        assertNotNull(createdReposByMembersProcessor.getDescription(nodesRepositories));
        assertEquals("", createdReposByMembersProcessor.getDescription(nodesRepositories));
    }

    @Test
    public void checkIfDescriptionIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();

        nodesRepositories.setDescription("This is a TestDescription");

        assertNotNull(createdReposByMembersProcessor.getDescription(nodesRepositories));
        assertEquals("This is a TestDescription", createdReposByMembersProcessor.getDescription(nodesRepositories));
    }

    @Test
    public void checkIfRequestQueryIsAssignedCorrectly() {
        createdReposByMembersProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(testQuery, createdReposByMembersProcessor.getRequestQuery());
    }

    @Test
    public void checkIfRequestRepositoryIsAssignedCorrectly() {
        createdReposByMembersProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.requestRepository, createdReposByMembersProcessor.getRequestRepository());
    }

    @Test
    public void checkIfOrganizationRepositoryIsAssignedCorrectly() {
        createdReposByMembersProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.organizationRepository, createdReposByMembersProcessor.getOrganizationRepository());
    }

    @Test
    public void checkIfOrganizationIsAssignedCorrectly() {
        OrganizationWrapper organizationWrapper = new OrganizationWrapper("adessoAG");
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);

        createdReposByMembersProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);

        assertSame(organizationWrapper, createdReposByMembersProcessor.getOrganization());
    }

    @Test
    public void checkIfStopAfterProcessingAllRepositories() {
        PageInfoRepositories pageInfoRepositories = new PageInfoRepositories();
        pageInfoRepositories.setHasNextPage(false);
        this.createdReposByMembersProcessor.setUp(testQuery, requestRepository, organizationRepository);
        this.createdReposByMembersProcessor.processRemainingRepositoriesOfMember(pageInfoRepositories, "adessoAG", "MDQ6VXNlcjkxOTkw");

        assertTrue(this.requestRepository.findAll().isEmpty());
        verify(requestRepository, times(0)).save(Mockito.any(Query.class));
    }

    @Test
    public void checkIfRemainingRepositoriesAreProcessed() {
        PageInfoRepositories pageInfoRepositories = new PageInfoRepositories();
        pageInfoRepositories.setHasNextPage(true);
        pageInfoRepositories.setEndCursor("testEndCursor");
        this.createdReposByMembersProcessor.setUp(testQuery, requestRepository, organizationRepository);
        this.createdReposByMembersProcessor.processRemainingRepositoriesOfMember(pageInfoRepositories, "adessoAG", "MDQ6VXNlcjkxOTkw");

        verify(requestRepository, times(1)).save(Mockito.any(Query.class));
    }

    @Test
    public void checkIfRequestTypeIsFinishedAfterLastRequest() {
        OrganizationWrapper organizationWrapper = new OrganizationWrapper("adessoAG");
        OrganizationDetail mockOrganizationDetail = mock(OrganizationDetail.class);
        organizationWrapper.setOrganizationDetail(mockOrganizationDetail);
        ArrayList<Query> queries = new ArrayList<>();
        queries.add(testQuery);
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);
        Mockito.when(requestRepository.findByQueryRequestTypeAndOrganizationName(RequestType.CREATED_REPOS_BY_MEMBERS, "adessoAG")).thenReturn(queries);
        Mockito.doNothing().when(mockOrganizationDetail).setNumOfCreatedReposByMembers(0);

        this.createdReposByMembersProcessor.setUp(testQuery, requestRepository, organizationRepository);
        this.createdReposByMembersProcessor.checkIfRequestTypeIsFinished();

        assertTrue(organizationWrapper.getFinishedRequests().contains(RequestType.CREATED_REPOS_BY_MEMBERS));
        assertEquals(0, this.createdReposByMembersProcessor.getCreatedRepositoriesByMembers().size());
    }

    @Test
    public void checkIfGeneralResponseProcessingIsCorrectly() {

    }
}