package processors;

import objects.ChartJSData;
import objects.Member;
import objects.Query;
import objects.ResponseWrapper;
import resources.member_Resources.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemberProcessor {

    private Query requestQuery;

    public MemberProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<Member> members = new ArrayList<>();
        ArrayList<NodesMember> membersData = this.requestQuery.getQueryResponse().getResponseMember().getData().getNodes();

        ArrayList<Date> pullRequestDates = new ArrayList<>();
        ArrayList<Date> issuesDates = new ArrayList<>();
        ArrayList<Date> commitsDates = new ArrayList<>();

        //TODO: Sort the arrays before creating ChartJSData to fit ascending order of the dates
        for (NodesMember singleMember : membersData) {
            for (NodesPullRequests nodesPullRequests : singleMember.getPullRequests().getNodes()) {
                if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesPullRequests.getCreatedAt().getTime()) {
                    pullRequestDates.add(nodesPullRequests.getCreatedAt());
                }
            }
            for (NodesIssues nodesIssues : singleMember.getIssues().getNodes()) {
                if (new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)).getTime() < nodesIssues.getCreatedAt().getTime()) {
                    issuesDates.add(nodesIssues.getCreatedAt());
                }
            }
            for (NodesRepoContributedTo nodesRepoContributedTo : singleMember.getRepositoriesContributedTo().getNodes()) {
                for (NodesHistory nodesHistory : nodesRepoContributedTo.getDefaultBranchRef().getTarget().getHistory().getNodes()) {
                    commitsDates.add(nodesHistory.getCommittedDate());
                }
            }

            members.add(new Member(singleMember.getName(), singleMember.getLogin(), singleMember.getAvatarUrl(), singleMember.getUrl(), generateChartJSData(commitsDates), generateChartJSData(issuesDates), generateChartJSData(pullRequestDates)));
        }

        return new ResponseWrapper(members);
    }

    private ChartJSData generateChartJSData(ArrayList<Date> arrayOfDates) {
        ArrayList<String> chartJSLabels = new ArrayList<>();
        ArrayList<Integer> chartJSDataset = new ArrayList<>();
        Boolean initalizeStartDate = true;
        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        // Make lists comparable and sort them
        Comparator<Date> byDate = (Date d1, Date d2) -> d1.compareTo(d2);
        Collections.sort(arrayOfDates, byDate);

        for (Date date : arrayOfDates) {
            Date oneWeekAgo = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
            Date currentDate = new Date(System.currentTimeMillis());

            //TODO: Teilweise wird ignoriert der 07 wenn 08 vorhanden ist
            if(oneWeekAgo.getTime() < date.getTime() && initalizeStartDate){
                for (int x = 0; x < (((date.getTime() - oneWeekAgo.getTime()) / DAY_IN_MS)); x++) {
                    chartJSLabels.add(this.getFormattedDate(new Date(oneWeekAgo.getTime() +  DAY_IN_MS*x)));
                    chartJSDataset.add(0);
                }
                initalizeStartDate = false;
            }

            String formattedDate = this.getFormattedDate(date);
            if (!chartJSLabels.contains(formattedDate)) {
                chartJSLabels.add(formattedDate);
                chartJSDataset.add(1);
            } else {
                chartJSDataset.set(chartJSLabels.indexOf(formattedDate), chartJSDataset.get(chartJSLabels.indexOf(formattedDate)) + 1);
            }

            if(arrayOfDates.size() != arrayOfDates.indexOf(date) + 1){
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date selectedDate = date;
                Date followingDateInArray = arrayOfDates.get(arrayOfDates.indexOf(date) + 1);
                try {
                    selectedDate = formatter.parse(formatter.format(selectedDate));
                    followingDateInArray = formatter.parse(formatter.format(followingDateInArray));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int x = 1; x < (((followingDateInArray.getTime() - selectedDate.getTime()) / DAY_IN_MS)); x++) {
                    chartJSLabels.add(this.getFormattedDate(new Date(date.getTime() +  DAY_IN_MS*x)));
                    chartJSDataset.add(0);
                }
            }

            if(arrayOfDates.size()-1 == arrayOfDates.indexOf(date) && currentDate.getTime() > date.getTime()){
                for (long x = (((currentDate.getTime() - date.getTime()) / DAY_IN_MS)); x >= 0; x--) {
                    chartJSLabels.add(this.getFormattedDate(new Date(currentDate.getTime() -  DAY_IN_MS*x)));
                    chartJSDataset.add(0);
                }
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
