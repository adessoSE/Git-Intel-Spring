package de.adesso.gitstalker.core.objects;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Team {

    private String name;
    private String description;
    private String avatarURL;
    private String githubURL;
    private ArrayList<Member> teamMembers;
    private ArrayList<Repository> teamRepositories;

    public Team(String name, String description, String avatarURL, String githubURL, ArrayList<Member> teamMembers, ArrayList<Repository> teamRepositories) {
        this.setName(name);
        this.setDescription(description);
        this.setAvatarURL(avatarURL);
        this.setGithubURL(githubURL);
        this.setTeamMembers(teamMembers);
        this.setTeamRepositories(teamRepositories);
    }
}
