package resources.memberIDResources;

import resources.Level1.Level2.Nodes;

import java.util.ArrayList;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private Organization organization;
    private ArrayList<Nodes> nodes;

    public Data() {}

    public Organization getOrganization() {
        return organization;
    }

    public ArrayList<Nodes> getNodes() {
        return nodes;
    }
}
