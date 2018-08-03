package resources.memberID_Resources;

import resources.memberPR_Resources.NodesMember;
import resources.rateLimit_Resources.RateLimit;

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
