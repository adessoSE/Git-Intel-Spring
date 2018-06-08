package processors;

import objects.Query;
import objects.RepositoryID;
import objects.ResponseWrapper;
import resources.repositoryID_Resources.NodesRepository;
import resources.repositoryID_Resources.Repositories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RepositoryIDProcessor {

    private Query requestQuery;

    public RepositoryIDProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<String> repositoryIDs = new ArrayList<>();
        Repositories repository = this.requestQuery.getQueryResponse().getResponseOrganRepoID().getData().getOrganization().getRepositories();
        for (NodesRepository nodes : repository.getNodes()) {
            repositoryIDs.add(nodes.getId());
        }
        return new ResponseWrapper(new RepositoryID(repositoryIDs, repository.getPageInfo().getEndCursor(), repository.getPageInfo().isHasNextPage()));
    }
}
