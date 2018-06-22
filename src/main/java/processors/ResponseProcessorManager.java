package processors;

import enums.ResponseProcessor;
import objects.Query;
import objects.ResponseWrapper;

public class ResponseProcessorManager {

    private Query requestQuery;

    public ResponseProcessorManager(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Manager for the selection of the suitable response processor. Selection is based on the ResponseProcessor specified in the query.
     * Returns the processed response in a ResponseWrapper because of the various responses.
     *
     * @return ResponseWrapper to fit all the different response in one object.
     */
    public ResponseWrapper processResponse() {
        ResponseProcessor responseProcessor = requestQuery.getQueryResponseProcessorType();
        switch (responseProcessor) {
            case ORGANIZATION_DETAIL:
                return new OrganizationDetailProcessor(this.requestQuery).processResponse();
            case MEMBER_ID:
                return new MemberIDProcessor(this.requestQuery).processResponse();
            case MEMBER:
                return new MemberProcessor(this.requestQuery).processResponse();
            case MEMBER_PR:
                return new MemberPRProcessor(this.requestQuery).processResponse();
            case REPOSITORY_ID:
                return new RepositoryIDProcessor(this.requestQuery).processResponse();
            case REPOSITORY:
                return new RepositoryProcessor(this.requestQuery).processResponse();
            case TEAM:
                return new TeamProcessor(this.requestQuery).processResponse();
            case EXTERNAL_REPO:
                return new ExternalRepoProcessor(this.requestQuery).processResponse();
            default:
                return new ResponseWrapper(this.requestQuery.getQueryResponse());
        }
    }
}
