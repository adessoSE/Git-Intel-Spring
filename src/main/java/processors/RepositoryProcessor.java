package processors;

import objects.Query;
import objects.Repository;
import objects.ResponseWrapper;
import resources.repository_Resources.*;

import java.util.ArrayList;
import java.util.Date;

public class RepositoryProcessor extends ResponseProcessor {

    private Query requestQuery;

    public RepositoryProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<Repository> repositories = new ArrayList<>();
        Repositories repositoriesData = this.requestQuery.getQueryResponse().getResponseRepository().getData().getOrganization().getRepositories();

        ArrayList<Date> pullRequestDates = new ArrayList<>();
        ArrayList<Date> issuesDates = new ArrayList<>();
        ArrayList<Date> commitsDates = new ArrayList<>();

        for (NodesRepositories repo : repositoriesData.getNodes()) {
            int stars = repo.getStargazers().getTotalCount();
            int forks = repo.getForkCount();
            String license = getLicense(repo);
            String programmingLanguage = getProgrammingLanguage(repo);
            String description = getDescription(repo);
            String name = repo.getName();

            for (NodesPullRequests nodesPullRequests : repo.getPullRequests().getNodes()) {
                if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesPullRequests.getCreatedAt().getTime()) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                }
            }
            for (NodesIssues nodesIssues : repo.getIssues().getNodes()) {
                if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesIssues.getCreatedAt().getTime()) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                }
            }
            if (repo.getDefaultBranchRef() != null) {
                for (NodesHistory nodesHistory : repo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }
            repositories.add(new Repository(name, description, programmingLanguage, license, forks, stars, this.generateChartJSData(commitsDates), this.generateChartJSData(issuesDates), this.generateChartJSData(pullRequestDates)));
        }
        return new ResponseWrapper(new objects.Repositories(repositories, repositoriesData.getPageInfo().getEndCursor(), repositoriesData.getPageInfo().isHasNextPage()));
    }

    private String getLicense(NodesRepositories repo) {
        if (repo.getLicenseInfo() == null) return "";
        else return repo.getLicenseInfo().getName();
    }

    private String getProgrammingLanguage(NodesRepositories repo) {
        if (repo.getPrimaryLanguage() == null) return "";
        else return repo.getPrimaryLanguage().getName();
    }

    private String getDescription(NodesRepositories repo) {
        if (repo.getDescription() == null) return "";
        else return repo.getDescription();
    }
}
