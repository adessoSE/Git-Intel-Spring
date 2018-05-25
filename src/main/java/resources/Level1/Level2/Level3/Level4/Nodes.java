package resources.Level1.Level2.Level3.Level4;

import resources.Level1.Level2.Level3.Level4.Level5.PullRequests;

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
