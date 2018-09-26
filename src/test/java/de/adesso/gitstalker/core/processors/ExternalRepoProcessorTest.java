package de.adesso.gitstalker.core.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Repository;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.LicenseInfo;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.NodesRepositories;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.PrimaryLanguage;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.ResponseExternalRepository;
import de.adesso.gitstalker.resources.ExternalRepoResources;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExternalRepoProcessorTest {

    private ExternalRepoProcessor externalRepoProcessor;
    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query testQuery = new Query("adessoAG", "testContent", RequestType.EXTERNAL_REPO, 1);
    private ResponseExternalRepository responseExternalRepository;
    private ExternalRepoResources externalRepoResources;

    @Before
    public void setUp() throws Exception {
        this.externalRepoResources = new ExternalRepoResources();
        this.responseExternalRepository = new ObjectMapper().readValue(this.externalRepoResources.getResponseExternalRepoRequest(), ResponseExternalRepository.class);
        this.requestRepository = mock(RequestRepository.class);
        this.organizationRepository = mock(OrganizationRepository.class);
        this.externalRepoProcessor = new ExternalRepoProcessor();
    }

    @Test
    public void checkIfRepositoriesAreProcessedCorrectly() {
        externalRepoProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        externalRepoProcessor.processQueryResponse(this.responseExternalRepository.getData().getNodes());

        Repository expectedRepository = new Repository("magento", "https://github.com/matthiasbalke/magento", "Magento Modules", "", "", 0, 1);
        Repository firstProcessedRepository = externalRepoProcessor.getRepositoriesMap().get("MDEwOlJlcG9zaXRvcnk2NDkzMzI=");

        assertEquals(expectedRepository.getName(), firstProcessedRepository.getName());
        assertEquals(expectedRepository.getDescription(), firstProcessedRepository.getDescription());
        assertEquals(expectedRepository.getUrl(), firstProcessedRepository.getUrl());
        assertEquals(expectedRepository.getLicense(), firstProcessedRepository.getLicense());
        assertEquals(expectedRepository.getProgrammingLanguage(), firstProcessedRepository.getProgrammingLanguage());
        assertEquals(expectedRepository.getForks(), firstProcessedRepository.getForks());
        assertEquals(expectedRepository.getStars(), firstProcessedRepository.getStars());
        assertEquals(2, externalRepoProcessor.getRepositoriesMap().size());
    }

    @Test
    public void checkIfRequestQueryIsAssignedCorrectly() {
        externalRepoProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(testQuery, externalRepoProcessor.getRequestQuery());
    }

    @Test
    public void checkIfRequestRepositoryIsAssignedCorrectly() {
        externalRepoProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.requestRepository, externalRepoProcessor.getRequestRepository());
    }

    @Test
    public void checkIfOrganizationRepositoryIsAssignedCorrectly() {
        externalRepoProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);
        assertSame(this.organizationRepository, externalRepoProcessor.getOrganizationRepository());
    }

    @Test
    public void checkIfOrganizationIsAssignedCorrectly() {
        OrganizationWrapper organizationWrapper = new OrganizationWrapper("adessoAG");
        Mockito.when(organizationRepository.findByOrganizationName("adessoAG")).thenReturn(organizationWrapper);

        externalRepoProcessor.setUp(testQuery, this.requestRepository, this.organizationRepository);

        assertSame(organizationWrapper, externalRepoProcessor.getOrganization());
    }

    @Test
    public void checkIfLicenseIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setLicenseInfo(null);

        assertNotNull(externalRepoProcessor.getLicense(nodesRepositories));
        assertEquals("", externalRepoProcessor.getLicense(nodesRepositories));
    }

    @Test
    public void checkIfLicenseIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        LicenseInfo licenseInfo = new LicenseInfo();
        licenseInfo.setName("MIT");
        nodesRepositories.setLicenseInfo(licenseInfo);

        assertNotNull(externalRepoProcessor.getLicense(nodesRepositories));
        assertEquals("MIT", externalRepoProcessor.getLicense(nodesRepositories));
    }

    @Test
    public void checkIfProgrammingLanguageIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setPrimaryLanguage(null);

        assertNotNull(externalRepoProcessor.getProgrammingLanguage(nodesRepositories));
        assertEquals("", externalRepoProcessor.getProgrammingLanguage(nodesRepositories));
    }

    @Test
    public void checkIfProgrammingLanguageIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        PrimaryLanguage primaryLanguage = new PrimaryLanguage();
        primaryLanguage.setName("Java");
        nodesRepositories.setPrimaryLanguage(primaryLanguage);

        assertNotNull(externalRepoProcessor.getProgrammingLanguage(nodesRepositories));
        assertEquals("Java", externalRepoProcessor.getProgrammingLanguage(nodesRepositories));
    }

    @Test
    public void checkIfDescriptionIsNullConversionToEmptyString() {
        NodesRepositories nodesRepositories = new NodesRepositories();
        nodesRepositories.setDescription(null);

        assertNotNull(externalRepoProcessor.getDescription(nodesRepositories));
        assertEquals("", externalRepoProcessor.getDescription(nodesRepositories));
    }

    @Test
    public void checkIfDescriptionIsConvertedCorrectly() {
        NodesRepositories nodesRepositories = new NodesRepositories();

        nodesRepositories.setDescription("This is a TestDescription");

        assertNotNull(externalRepoProcessor.getDescription(nodesRepositories));
        assertEquals("This is a TestDescription", externalRepoProcessor.getDescription(nodesRepositories));
    }
}