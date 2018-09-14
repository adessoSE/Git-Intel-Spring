package de.adesso.gitstalker.core.resources.createdReposByMembers;

public class PageInfoRepositories {

    private boolean hasNextPage;
    private String endCursor;

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public String getEndCursor() {
        return endCursor;
    }
}
