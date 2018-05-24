package processors;

import objects.Query;
import objects.RepositoryID;
import objects.ResponseWrapper;

import java.util.ArrayList;

public class RepositoryIDProcessor {

    private Query requestQuery;

    public RepositoryIDProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> repositoryIDs = new ArrayList<>();
        this.requestQuery.getQueryResponse().getOrganization().getRepositories().getNodes().forEach(nodes -> repositoryIDs.add(nodes.getId()) );
        return new ResponseWrapper(new RepositoryID(repositoryIDs,this.requestQuery.getQueryResponse().getOrganization().getRepositories().getPageInfo().getEndCursor(),this.requestQuery.getQueryResponse().getOrganization().getRepositories().getPageInfo().isHasNextPage()));
    }
}
