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
    private String responseExternalRepoRequest = "{\n" +
            "  \"data\": {\n" +
            "    \"nodes\": [\n" +
            "      {\n" +
            "        \"url\": \"https://github.com/matthiasbalke/magento\",\n" +
            "        \"id\": \"MDEwOlJlcG9zaXRvcnk2NDkzMzI=\",\n" +
            "        \"name\": \"magento\",\n" +
            "        \"description\": \"Magento Modules\",\n" +
            "        \"forkCount\": 0,\n" +
            "        \"stargazers\": {\n" +
            "          \"totalCount\": 1\n" +
            "        },\n" +
            "        \"licenseInfo\": null,\n" +
            "        \"primaryLanguage\": null,\n" +
            "        \"defaultBranchRef\": {\n" +
            "          \"target\": {\n" +
            "            \"history\": {\n" +
            "              \"nodes\": []\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"pullRequests\": {\n" +
            "          \"nodes\": []\n" +
            "        },\n" +
            "        \"issues\": {\n" +
            "          \"nodes\": [\n" +
            "            {\n" +
            "              \"createdAt\": \"2012-07-10T18:43:03Z\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"url\": \"https://github.com/matthiasbalke/artikel-bdd-2013\",\n" +
            "        \"id\": \"MDEwOlJlcG9zaXRvcnkxMDk0NjQ1Mw==\",\n" +
            "        \"name\": \"artikel-bdd-2013\",\n" +
            "        \"description\": \"Example project for the article 'Behavior Driven Development' by Matthias Balke und Sebastian Laag\",\n" +
            "        \"forkCount\": 0,\n" +
            "        \"stargazers\": {\n" +
            "          \"totalCount\": 0\n" +
            "        },\n" +
            "        \"licenseInfo\": null,\n" +
            "        \"primaryLanguage\": {\n" +
            "          \"name\": \"Java\"\n" +
            "        },\n" +
            "        \"defaultBranchRef\": {\n" +
            "          \"target\": {\n" +
            "            \"history\": {\n" +
            "              \"nodes\": []\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"pullRequests\": {\n" +
            "          \"nodes\": []\n" +
            "        },\n" +
            "        \"issues\": {\n" +
            "          \"nodes\": []\n" +
            "        }\n" +
            "      }\n" +
            "    ],\n" +
            "    \"rateLimit\": {\n" +
            "      \"cost\": 1,\n" +
            "      \"remaining\": 4999,\n" +
            "      \"resetAt\": \"2018-09-25T06:44:16Z\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.responseExternalRepository = objectMapper.readValue(responseExternalRepoRequest, ResponseExternalRepository.class);
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