package processors;

import resources.organisation_Resources.Organization;
import objects.OrganizationDetail;
import objects.Query;
import objects.ResponseWrapper;

public class OrganizationDetailProcessor {

    private Query requestQuery;

    public OrganizationDetailProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        Organization organization = this.requestQuery.getQueryResponse().getResponseOrganization().getData().getOrganization();
        return new ResponseWrapper(new OrganizationDetail(organization.getName(),organization.getDescription(),organization.getWebsiteUrl(),organization.getUrl(),organization.getLocation(),organization.getMembers().getTotalCount(),organization.getRepositories().getTotalCount(),organization.getTeams().getTotalCount()));
    }
}
