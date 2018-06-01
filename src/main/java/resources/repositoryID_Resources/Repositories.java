package resources.repositoryID_Resources;

import java.util.ArrayList;

public class Repositories {

    private PageInfo pageInfo;
    private ArrayList<NodesRepository> nodes;

    public Repositories() {
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public ArrayList<NodesRepository> getNodes() {
        return this.nodes;
    }
}
