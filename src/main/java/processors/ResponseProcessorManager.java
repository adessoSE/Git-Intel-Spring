package processors;

import enums.ResponseProcessor;
import objects.Query;
import objects.ResponseWrapper;

public class ResponseProcessorManager {

    private Query requestQuery;

    public ResponseProcessorManager(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ResponseProcessor responseProcessor = requestQuery.getQueryResponseProcessorType();
        switch (responseProcessor) {
            case MEMBER_PR:
                return new MemberPRProcessor(this.requestQuery).processResponse();
            case ORGANIZATION_DETAIL:
                return new OrganizationDetailProcessor(this.requestQuery).processResponse();
            case MEMBER_ID:
                return new MemberIDProcessor(this.requestQuery).processResponse();
            case REPOSITORY_ID:
                return new RepositoryIDProcessor(this.requestQuery).processResponse();
            case MEMBER:
                return new MemberProcessor(this.requestQuery).processResponse();
            default:
                return new ResponseWrapper(this.requestQuery.getQueryResponse());
        }
    }
}
