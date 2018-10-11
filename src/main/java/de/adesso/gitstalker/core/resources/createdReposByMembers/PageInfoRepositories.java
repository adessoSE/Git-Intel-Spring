package de.adesso.gitstalker.core.resources.createdReposByMembers;

import com.fasterxml.jackson.annotation.JsonProperty;


public class PageInfoRepositories {

    @JsonProperty("hasNextPage")
    private boolean hasNextPage;
    private String endCursor;

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }
}
