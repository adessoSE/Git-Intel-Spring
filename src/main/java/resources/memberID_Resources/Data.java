package resources.memberID_Resources;

import resources.memberPR_Resources.NodesMember;

import java.util.ArrayList;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private Organization organization;
    private ArrayList<NodesMember> nodes;

    public Data() {}

    public Organization getOrganization() {
        return organization;
    }

    public ArrayList<NodesMember> getNodes() {
        return nodes;
    }
}
