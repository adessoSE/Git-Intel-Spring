package de.adesso.gitstalker.core.resources.member_Resources;


import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;

public class Data {

    private User node;
    private RateLimit rateLimit;

    public Data() {}

    public User getNode() {
        return node;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }
}
