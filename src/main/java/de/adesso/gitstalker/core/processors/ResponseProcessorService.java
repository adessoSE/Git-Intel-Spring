package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the Response manager that carries the different processors within itself.
 */
@Service
public class ResponseProcessorService {

    private OrganizationValidationProcessor organizationValidationProcessor;
    private OrganizationDetailProcessor organizationDetailProcessor;
    private MemberIDProcessor memberIDProcessor;
    private MemberProcessor memberProcessor;
    private MemberPRProcessor memberPRProcessor;
    private RepositoryProcessor repositoryProcessor;
    private TeamProcessor teamProcessor;
    private ExternalRepoProcessor externalRepoProcessor;
    private CreatedReposByMembersProcessor createdReposByMembersProcessor;

    @Autowired
    public ResponseProcessorService(OrganizationValidationProcessor organizationValidationProcessor,
                                    OrganizationDetailProcessor organizationDetailProcessor,
                                    MemberIDProcessor memberIDProcessor,
                                    MemberProcessor memberProcessor,
                                    MemberPRProcessor memberPRProcessor,
                                    RepositoryProcessor repositoryProcessor,
                                    TeamProcessor teamProcessor,
                                    ExternalRepoProcessor externalRepoProcessor,
                                    CreatedReposByMembersProcessor createdReposByMembersProcessor) {
        this.organizationValidationProcessor = organizationValidationProcessor;
        this.organizationDetailProcessor = organizationDetailProcessor;
        this.memberIDProcessor = memberIDProcessor;
        this.memberProcessor = memberProcessor;
        this.memberPRProcessor = memberPRProcessor;
        this.repositoryProcessor = repositoryProcessor;
        this.teamProcessor = teamProcessor;
        this.externalRepoProcessor = externalRepoProcessor;
        this.createdReposByMembersProcessor = createdReposByMembersProcessor;
    }

    /**
     * The central routing of requests to processors is controlled here.
     *
     * @param requestQuery Query to be processed.
     */
    public void processResponse(Query requestQuery) {
        switch (requestQuery.getQueryRequestType()) {
            case ORGANIZATION_VALIDATION:
                this.organizationValidationProcessor.processResponse(requestQuery);
                break;
            case ORGANIZATION_DETAIL:
                this.organizationDetailProcessor.processResponse(requestQuery);
                break;
            case MEMBER_ID:
                this.memberIDProcessor.processResponse(requestQuery);
                break;
            case MEMBER:
                this.memberProcessor.processResponse(requestQuery);
                break;
            case MEMBER_PR:
                this.memberPRProcessor.processResponse(requestQuery);
                break;
            case REPOSITORY:
                this.repositoryProcessor.processResponse(requestQuery);
                break;
            case TEAM:
                this.teamProcessor.processResponse(requestQuery);
                break;
            case EXTERNAL_REPO:
                this.externalRepoProcessor.processResponse(requestQuery);
                break;
            case CREATED_REPOS_BY_MEMBERS:
                this.createdReposByMembersProcessor.processResponse(requestQuery);
                break;
        }
    }
}
