package de.adesso.gitstalker.core.objects;

import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import de.adesso.gitstalker.core.requests.Request;

@Getter
@Setter
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
        this.setOrganizationName(organizationName);
        this.setQueryStatus(RequestStatus.CREATED);
        this.setQuery(queryContent);
        this.setQueryRequestType(queryRequestType);
        this.setEstimatedQueryCost(estimatedQueryCost);
    }

    public String getQuery() {
        return queryContent;
    }

    public void setQuery(String queryContent) {
        this.queryContent = queryContent;
    }

    public void crawlQueryResponse() {
        this.crawlData(this);
    }
}