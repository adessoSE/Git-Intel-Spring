package entities.Level1.Level2.Level3.Level4;

import entities.Level1.Level2.Level3.Level4.Level5.PullRequests;

import java.util.ArrayList;

public class Nodes {

    private String company;
    private String login;
    private PullRequests pullRequests;
    private String id;

    public Nodes() {

    }


    public String getId() {
        return id;
    }

    public String getCompany() { return company; }

    public String getLogin() { return login; }

    public PullRequests getPullRequests() {
        return pullRequests;
    }
}
