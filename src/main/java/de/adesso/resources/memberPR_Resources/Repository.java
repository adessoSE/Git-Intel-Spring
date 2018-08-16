package resources.memberPR_Resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Repository {

    private String id;

    @JsonProperty("isFork")
    private boolean isFork;

    public Repository(){}

    public String getId() {
        return id;
    }

    public boolean isFork() {
        return isFork;
    }
}
