package resources.team_Resources;


import java.util.ArrayList;

public class NodesTeams {

    private String name;
    private String description;
    private String avatarURL;
    private Repositories repositories;
    private Members members;


    public Repositories getRepositories() {
        return repositories;
    }

    public String getName() {
        return name;
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