package objects.Team;

import java.util.ArrayList;

public class Team {

    private String name;
    private String description;
    private String avatarURL;
    private String githubURL;
    private int numOfMembers;
    private ArrayList<TeamRepository> teamRepositories;

    public Team(String name, String description, String avatarURL, String githubURL, int numOfMembers, ArrayList<TeamRepository> teamRepositories) {
        this.name = name;
        this.description = description;
        this.avatarURL = avatarURL;
        this.githubURL = githubURL;
        this.numOfMembers = numOfMembers;
        this.teamRepositories = teamRepositories;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public ArrayList<TeamRepository> getTeamRepositories() {
        return teamRepositories;
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

    public int getNumOfMembers() {
        return numOfMembers;
    }
}
