package processors;

import objects.ChartJSData;
import objects.Query;
import objects.Repository;
import objects.ResponseWrapper;
import resources.repository_Resources.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class RepositoryProcessor {

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

        //TODO: Seperate in various methods!
        for (NodesRepositories repo : repositoriesData.getNodes()) {
            int stars = repo.getStargazers().getTotalCount();
            int forks = repo.getForkCount();

            String url = repo.getUrl();
            String license;
            if (repo.getLicenseInfo() == null) license = "";
            else license = repo.getLicenseInfo().getName();

            String programmingLanguage;
            if (repo.getPrimaryLanguage() == null) programmingLanguage = "";
            else programmingLanguage = repo.getPrimaryLanguage().getName();

            String description;
            if (repo.getDescription() == null) description = "";
            else description = repo.getDescription();

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
            if(repo.getDefaultBranchRef() != null) {
                for (NodesHistory nodesHistory : repo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }
            repositories.add(new Repository(name, url, description, programmingLanguage, license, forks, stars, generateChartJSData(commitsDates), generateChartJSData(issuesDates), generateChartJSData(pullRequestDates)));
        }

        return new ResponseWrapper(new objects.Repositories(repositories, repositoriesData.getPageInfo().getEndCursor(), repositoriesData.getPageInfo().isHasNextPage()));
    }

    private ChartJSData generateChartJSData(ArrayList<Date> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();

        for (Date date : arrayOfDates) {
            String formattedDate = this.getFormattedDate(date);
            if (!chartJSLabels.contains(formattedDate)) {
                chartJSLabels.add(formattedDate);
                chartJSDataset.add(1);
            } else {
                chartJSDataset.set(chartJSLabels.indexOf(formattedDate), chartJSDataset.get(chartJSLabels.indexOf(formattedDate)) + 1);
            }
        }

        return new ChartJSData(chartJSLabels, chartJSDataset);
    }

    private String getFormattedDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        return df.format(date);
    }
}
