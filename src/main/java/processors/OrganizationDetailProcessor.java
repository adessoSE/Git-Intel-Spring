package processors;

import config.RateLimitConfig;
import objects.OrganizationDetail;
import objects.Query;
import objects.ResponseWrapper;
import resources.organisation_Resources.Organization;
import resources.rateLimit_Resources.RateLimit;

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
        RateLimit rateLimit = this.requestQuery.getQueryResponse().getResponseOrganization().getData().getRateLimit();
        RateLimitConfig.setRemainingRateLimit(rateLimit.getRemaining());
        RateLimitConfig.setResetRateLimitAt(rateLimit.getResetAt());
        RateLimitConfig.addPreviousRequestCostAndRequestType(rateLimit.getCost(),requestQuery.getQueryRequestType());

        System.out.println("Rate Limit:"  + RateLimitConfig.getRateLimit());
        System.out.println("Rate Limit Remaining:"  + RateLimitConfig.getRemainingRateLimit());
        System.out.println("Request Cost:"  + RateLimitConfig.getPreviousRequestCostAndRequestType());
        System.out.println("Reset Rate Limit At: " + RateLimitConfig.getResetRateLimitAt());

        Organization organization = this.requestQuery.getQueryResponse().getResponseOrganization().getData().getOrganization();
        return new ResponseWrapper(new OrganizationDetail(
                organization.getName(),
                organization.getDescription(),
                organization.getWebsiteUrl(),
                organization.getUrl(),
                organization.getLocation(),
                organization.getAvatarUrl(),
                organization.getMembers().getTotalCount(),
                organization.getRepositories().getTotalCount(),
                organization.getTeams().getTotalCount()));
    }
}
