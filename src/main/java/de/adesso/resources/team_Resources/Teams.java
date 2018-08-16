package de.adesso.resources.team_Resources;

import java.util.ArrayList;

public class Teams {

    private PageInfo pageInfo;
    private ArrayList<NodesTeams> nodes;
    private int totalCount;


    public Teams() {
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public ArrayList<NodesTeams> getNodes() {
        return this.nodes;
    }

    public int getTotalCount() {
        return totalCount;
    