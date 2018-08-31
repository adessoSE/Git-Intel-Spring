package objects.Team;

import objects.Member;
import objects.Repository;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {

    private String name;
    private String description;
    private String avatarURL;
    private String githubURL;
    private ArrayList<Member> teamMembers;
    private ArrayList<Repository> teamRepositories;

    public Team(String name, String description, String avatarURL, String githubURL, ArrayList<Member> teamMembers, ArrayList<Repository> teamRepositories) {
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

    public ArrayList<Member> getTeamMembers() {
        return teamMembers;
    }

    public ArrayList<Repository> getTeamRepositories() {
        return teamRepositories;
    }
}
