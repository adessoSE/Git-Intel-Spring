package objects.Team;

import objects.Member;
import objects.Repository;

import java.util.HashMap;

public class Team {

    private String name;
    private String description;
    private String avatarURL;
    private String githubURL;
    private HashMap<String, Member> teamMembers;
    private HashMap<String, Repository> teamRepositories;

    public Team(String name, String description, String avatarURL, String githubURL, HashMap<String, Member> teamMembers, HashMap<String, Repository> teamRepositories) {
        this.name = name;
        this.description = description;
        this.avatarURL = avatarURL;
        this.githubURL = githubURL;
        this.teamMembers = teamMembers;
        this.teamRepositories = teamRepositories;
    }

    public String getGithubURL() {
        return githubURL;
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

    public HashMap<String, Member> getTeamMembers() {
        return teamMembers;
    }

    public HashMap<String, Repository> getTeamRepositories() {
        return teamRepositories;
    }
}
