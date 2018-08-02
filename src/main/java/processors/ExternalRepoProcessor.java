package processors;

import config.Config;
import objects.Query;
import objects.Repositories;
import objects.Repository;
import objects.ResponseWrapper;
import resources.externalRepo_Resources.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ExternalRepoProcessor extends ResponseProcessor {

    private Query requestQuery;

    public ExternalRepoProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        HashMap<String,Repository> repositoriesMap = new HashMap<>();
        Data repositoriesData = this.requestQuery.getQueryResponse().getResponseExternalRepository().getData();

        ArrayList<Date> pullRequestDates = new ArrayList<>();
        ArrayList<Date> issuesDates = new ArrayList<>();
        ArrayList<Date> commitsDates = new ArrayList<>();

        for (NodesRepositories repo : repositoriesData.getNodes()) {
            int stars = repo.getStargazers().getTotalCount();
            int forks = repo.getForkCount();
            String url = repo.getUrl();
            String license = getLicense(repo);
            String programmingLanguage = getProgrammingLanguage(repo);
            String description = getDescription(repo);
            String name = repo.getName();
            String id = repo.getId();

            for (NodesPullRequests nodesPullRequests : repo.getPullRequests().getNodes()) {
                if (new Date(System.currentTimeMillis() - (Config.PAST_DAYS_AMOUNT_TO_CRAWL * 1000 * 60 * 60 * 24)).getTime() < nodesPullRequests.getCreatedAt().getTime()) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                }
            }
            for (NodesIssues nodesIssues : repo.getIssues().getNodes()) {
                if (new Date(System.currentTimeMillis() - (Config.PAST_DAYS_AMOUNT_TO_CRAWL * 1000 * 60 * 60 * 24)).getTime() < nodesIssues.getCreatedAt().getTime()) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                }
            }
            if (repo.getDefaultBranchRef() != null) {
                for (NodesHistory nodesHistory : repo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }
            repositoriesMap.put(id, new Repository(name, url, description, programmingLanguage, license, forks, stars, this.generateChartJSData(commitsDates), this.generateChartJSData(issuesDates), this.generateChartJSData(pullRequestDates)));
        }
        return new ResponseWrapper(new Repositories(repositoriesMap));
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
