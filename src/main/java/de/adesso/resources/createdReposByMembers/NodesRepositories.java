package resources.createdReposByMembers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodesRepositories {

    private String url;
    private String name;
    private String id;
    private String description;
    private int forkCount;
    private Stargazers stargazers;
    private LicenseInfo licenseInfo;
    private PrimaryLanguage primaryLanguage;
    @JsonProperty("isFork")
    private boolean isFork;
    @JsonProperty("isMirror")
    private boolean isMirror;
    private Owner owner;

    public String getUrl() {
        return url;
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

    public int getForkCount() {
        return forkCount;
    }

    public Stargazers getStargazers() {
        return stargazers;
    }

    public LicenseInfo getLicenseInfo() {
        return licenseInfo;
    }

    public PrimaryLanguage getPrimaryLanguage() {
        return primaryLanguage;
    }

    public boolean isFork() {
        return isFork;
    }

    public boolean isMirror() {
        return isMirror;
    }

    public Owner getOwner() {
        return owner;
    }

}
