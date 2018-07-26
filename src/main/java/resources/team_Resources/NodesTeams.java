package resources.team_Resources;


import java.util.ArrayList;

public class NodesTeams {

    private String name;
    private String id;
    private String description;
    private String avatarUrl;
    private Repositories repositories;
    private Members members;


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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Members getMembers() {
        return members;
    }
}
