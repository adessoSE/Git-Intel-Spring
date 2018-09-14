package de.adesso.gitstalker.core.resources.memberID_Resources;

import de.adesso.gitstalker.core.resources.memberPR_Resources.NodesMember;
import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;

import java.util.ArrayList;

public class Data {

    private Organization organization;
    private ArrayList<NodesMember> nodes;
    private RateLimit rateLimit;

    public Data() {}

    public Organization getOrganization() {
        return organization;
    }

    public ArrayList<NodesMember> getNodes() {
        return nodes;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }
}
