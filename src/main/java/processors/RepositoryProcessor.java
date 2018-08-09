package processors;

import config.Config;
import objects.Query;
import objects.Repository;
import objects.ResponseWrapper;
import resources.repository_Resources.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RepositoryProcessor extends ResponseProcessor {

    private Query requestQuery;

    public RepositoryProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        super.updateRateLimit(this.requestQuery.getQueryResponse().getResponseRepository().getData().getRateLimit(), requestQuery.getQueryRequestType());

        HashMap<String, Repository> repositories = new HashMap<>();
        Repositories repositoriesData = this.requestQuery.getQueryResponse().getResponseRepository().getData().getOrganization().getRepositories();

        ArrayList<Calendar> pullRequestDates = new ArrayList<>();
        ArrayList<Calendar> issuesDates = new ArrayList<>();
        ArrayList<Calendar> commitsDates = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.get(Calendar.DATE)-Config.PAST_DAYS_AMOUNT_TO_CRAWL);

        for (NodesRepositories repo : repositoriesData.getNodes()) {
            int stars = repo.getStargazers().getTotalCount();
            int forks = repo.getForkCount();
            String url = repo.getUrl();
            String license = getLicense(repo);
            String programmingLanguage = getProgrammingLanguage(repo);
            String description = getDescription(repo);
            String name = repo.getName();

            for (NodesPullRequests nodesPullRequests : repo.getPullRequests().getNodes()) {
                if (cal.before(nodesPullRequests.getCreatedAt())) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                }
            }
            for (NodesIssues nodesIssues : repo.getIssues().getNodes()) {
                if (cal.before(nodesIssues.getCreatedAt())) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                }
            }
            if (repo.getDefaultBranchRef() != null) {
                for (NodesHistory nodesHistory : repo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }
            repositories.put(repo.getId(), new Repository(name, url, description, programmingLanguage, license, forks, stars, this.generateChartJSData(commitsDates), this.generateChartJSData(issuesDates), this.generateChartJSData(pullRequestDates)));
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
