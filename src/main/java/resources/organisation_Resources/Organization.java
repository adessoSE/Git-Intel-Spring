package resources.organisation_Resources;

public class Organization {

    private String name;
    private String location;
    private String websiteUrl;
    private String url;
    private String avatarUrl;
    private String description;
    private Members members;
    private Teams teams;
    private Repositories repositories;

    public Organization() {}

    public String getAvatarUrl() {
        return avatarUrl;
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
