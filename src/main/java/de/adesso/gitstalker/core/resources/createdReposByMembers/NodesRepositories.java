package de.adesso.gitstalker.core.resources.createdReposByMembers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
