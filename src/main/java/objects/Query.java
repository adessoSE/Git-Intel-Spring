package objects;

import entities.Level1.Data;
import enums.RequestStatus;
import enums.ResponseProcessor;
import org.springframework.data.annotation.Id;


public class Query {
    @Id
    private int id;

    private RequestStatus queryStatus;
    private String organizationName;
    private String queryContent;
    private Data queryResponse;
    private ResponseProcessor queryResponseProcessorType;
    private String queryError;

    public Query(String organizationName, String queryContent, ResponseProcessor queryResponseProcessorType) {
        this.setOrganizationName(organizationName);
        this.setQueryStatus(RequestStatus.CREATED);
        this.setQuery(queryContent);
        this.setQueryResponseProcessorType(queryResponseProcessorType);
    }

    public RequestStatus getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(RequestStatus queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getQuery() {
        return queryContent;
    }

    public void setQuery(String queryContent) {
        this.queryContent = queryContent;
    }

    public Data getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(Data queryResponse) {
        this.queryResponse = queryResponse;
    }

    public String getQueryError() {
        return queryError;
    }

    public void setQueryError(String queryError) {
        this.queryError = queryError;
    }

    public ResponseProcessor getQueryResponseProcessorType() {
        return queryResponseProcessorType;
    }

    public void setQueryResponseProcessorType(ResponseProcessor queryResponseProcessorType) {
        this.queryResponseProcessorType = queryResponseProcessorType;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

}
