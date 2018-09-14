package de.adesso.gitstalker.core.objects;

import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import org.springframework.data.annotation.Id;
import de.adesso.gitstalker.core.requests.Request;


public class Query extends Request {
    @Id
    private String id;

    private int estimatedQueryCost;
    private RequestStatus queryStatus;
    private String organizationName;
    private String queryContent;
    private Response queryResponse;
    private RequestType queryRequestType;
    private Exception queryError;

    public Query(String organizationName, String queryContent, RequestType queryRequestType, int estimatedQueryCost) {
        this.organizationName = organizationName;
        this.setQueryStatus(RequestStatus.CREATED);
        this.setQuery(queryContent);
        this.queryRequestType = queryRequestType;
        this.estimatedQueryCost = estimatedQueryCost;
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

    public Response getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(Response queryResponse) {
        this.queryResponse = queryResponse;
    }

    public void crawlQueryResponse() {
        this.crawlData(this);
    }

    public Exception getQueryError() {
        return queryError;
    }

    public void setQueryError(Exception queryError) {
        this.queryError = queryError;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public RequestType getQueryRequestType() {
        return queryRequestType;
    }

    public int getEstimatedQueryCost() {
        return estimatedQueryCost;
    }
}
