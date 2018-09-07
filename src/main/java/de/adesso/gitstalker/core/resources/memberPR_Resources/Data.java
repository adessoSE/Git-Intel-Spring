package de.adesso.gitstalker.core.resources.memberPR_Resources;

import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;

public class Data {

    private Organization organization;
    private RateLimit rateLimit;

    public Data() {}

    public Organization getOrganization() {
        return organization;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }
}
