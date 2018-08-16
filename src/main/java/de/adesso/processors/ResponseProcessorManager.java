package de.adesso.processors;

import de.adesso.enums.ResponseProcessor;
import de.adesso.objects.Query;
import de.adesso.objects.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseProcessorManager {

    private Query requestQuery;

    @Autowired
    private OrganizationValidationProcessor organizationValidationProcessor;

    public ResponseProcessorManager(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Manager for the selection of the suitable response processor. Selection is based on the ResponseProcessor specified in the query.
     * Returns the processed response in a ResponseWrapper because of the various responses.
     *
     * @return ResponseWrapper to fit all the different response in one object.
     */
    public ResponseWrapper processResponse(Query requestQuery) {
        ResponseProcessor responseProcessor = requestQuery.getQueryResponseProcessorType();
        switch (responseProcessor) {
            case ORGANIZATION_VALIDATION:
                organizationValidationProcessor.processResponse(this.requestQuery);
//                new OrganizationValidationProcessor(this.requestQuery).processResponse();
            case ORGANIZATION_DETAIL:
                return new OrganizationDetailProcessor(this.requestQuery).processResponse();
            case MEMBER_ID:
                return new MemberIDProcessor(this.requestQuery).processResponse();
            case MEMBER:
                return new MemberProcessor(this.requestQuery).processResponse();
            case MEMBER_PR:
                return new MemberPRProcessor(this.requestQuery).processResponse();
            case REPOSITORY:
                return new RepositoryProcessor(this.requestQuery).processResponse();
            case TEAM:
                return new TeamProcessor(this.requestQuery).processResponse();
            case EXTERNAL_REPO:
                return new ExternalRepoProcessor(this.requestQuery).processResponse();
            case CREATED_REPOS_BY_MEMBERS:
                return new CreatedReposByMembersProcessor(this.requestQuery).processResponse();
            default:
                return new ResponseWrapper(this.requestQuery.get