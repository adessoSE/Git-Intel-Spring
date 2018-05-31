package processors;

import objects.OrganizationDetail;
import objects.Query;
import objects.ResponseWrapper;
import resources.organisation_Resources.Organization;

public class OrganizationDetailProcessor {

    private Query requestQuery;

    public OrganizationDetailProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    /**
     * Response processing of the OrganizationDetail request.
     * Creating a OrganizationDetail object containing the detailed organization information wrapped into the ResponseWrapper.
     *
     * @return ResponseWrapper containing the OrganizationDetail object.
     */
    public ResponseWrapper processResponse() {
        Organization organization = this.requestQuery.getQueryResponse().getResponseOrganization().getData().getOrganization();
        return new ResponseWrapper(new OrganizationDetail(organization.getName(),
                organization.getDescription(),
                organization.getWebsiteUrl(),
                organization.getUrl(),
                organization.getLocation(),
                organization.getMembers().getTotalCount(),
                organization.getRepositories().getTotalCount(),
                organization.getTeams().getTotalCount()));
    }
}
