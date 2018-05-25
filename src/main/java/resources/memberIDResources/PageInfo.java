package resources.memberIDResources;

public class PageInfo {

    private boolean hasNextPage;
    private String endCursor;

    public PageInfo() {}

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public String getEndCursor() {
        return endCursor;
    }

}
