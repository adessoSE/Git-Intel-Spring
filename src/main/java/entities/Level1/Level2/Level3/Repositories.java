package entities.Level1.Level2.Level3;

import entities.Level1.Level2.Level3.Level4.Nodes;
import entities.Level1.Level2.Level3.Level4.PageInfo;

import java.util.ArrayList;

public class Repositories {

    private int totalCount;
    private ArrayList<Nodes> nodes;
    private PageInfo pageInfo;

    public Repositories() {}

    public int getTotalCount() {
        return totalCount;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public ArrayList<Nodes> getNodes() {
        return nodes;
    }

}
