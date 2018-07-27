package resources.team_Resources;


import java.util.ArrayList;

public class NodesTeams {

    private String name;
    private String id;
    private String description;
    private String avatarURL;
    private String url;
    private Repositories repositories;
    private Members members;

    public String getUrl() {
        return url;
    }

    public Repositories getRepositories() {
        return repositories;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public Members getMembers() {
        return members;
    }
}
