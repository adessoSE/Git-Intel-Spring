package entities.Level1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entities.Level1.Level2.Organization;
import entities.Level1.Level2.Viewer;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    private Viewer viewer;
    private Organization organization;

    public Data() {
    }

    public Viewer getViewer() {
        return viewer;
    }

    public Organization getOrganization() {
        return organization;
    }
}
