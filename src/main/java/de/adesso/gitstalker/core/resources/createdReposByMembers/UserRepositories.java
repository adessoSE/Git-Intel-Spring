package de.adesso.gitstalker.core.resources.createdReposByMembers;

import java.util.ArrayList;

public class UserRepositories {

    private PageInfoRepositories pageInfo;
    private ArrayList<NodesRepositories> nodes;

    public PageInfoRepositories getPageInfo() {
        return pageInfo;
    }

    public ArrayList<NodesRepositories> getNodes() {
        return nodes;
    }

}
