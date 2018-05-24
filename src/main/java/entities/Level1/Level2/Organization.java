package entities.Level1.Level2;

import entities.Level1.Level2.Level3.Members;
import entities.Level1.Level2.Level3.Repositories;
import entities.Level1.Level2.Level3.Teams;
import org.springframework.data.annotation.Id;

public class Organization {

    @Id
    private String id;

    private String name;
    private String location;
    private String websiteUrl;
    private String url;
    private String description;
    private Members members;
    private Teams teams;
    private Repositories repositories;

    public Organization() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Members getMembers() {
        return members;
    }

    public Teams getTeams() {
        return teams;
    }

    public Repositories getRepositories() {
        return repositories;
    }
}
