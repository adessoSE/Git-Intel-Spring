package entities.Level1;

import entities.Level1.Level2.Nodes;
import entities.Level1.Level2.Organization;

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
