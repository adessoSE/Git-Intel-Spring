package de.adesso.gitstalker.core.resources.createdReposByMembers;

import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;

public class Data {

    private NodeUser node;
    private RateLimit rateLimit;

    public NodeUser getNode() {
        return node;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }
}
