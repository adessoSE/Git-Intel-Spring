package objects;


import objects.Team.Teams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ResponseWrapper {

    private Response response;
    private MemberID memberID;
    private OrganizationDetail organizationDetail;
    private MemberPR memberPR;
    private HashMap<String, Member> members;
    private HashMap<String,ArrayList<Calendar>> comittedRepos;
    private HashMap<String,ArrayList<Calendar>> pullRequestsDates;
    private Repositories repositories;
    private Teams teams;
    private CreatedRepositoriesByMembers createdRepositoriesByMembers;
    private boolean isValid;

    // TODO: Builder Pattern anwenden! Mehrere Constructoren mit ArrayList nicht zulässig!

    public ResponseWrapper(boolean isValid) {
        this.isValid = isValid;
    }

    public ResponseWrapper(Teams teams) {
        this.teams = teams;
    }

    public ResponseWrapper(Repositories repositories) {
        this.repositories = repositories;
    }

    public ResponseWrapper(CreatedRepositoriesByMembers createdRepositoriesByMembers){
        this.createdRepositoriesByMembers = createdRepositoriesByMembers;
    }
    public ResponseWrapper(HashMap<String, Member> members, HashMap<String,ArrayList<Calendar>> committedRepos) {
        this.members = members;
        this.comittedRepos = committedRepos;
    }

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public ResponseWrapper(MemberPR memberPR, HashMap<String,ArrayList<Calendar>> pullRequestsDates) {
        this.memberPR = memberPR;
        this.pullRequestsDates = pullRequestsDates;
    }

    public ResponseWrapper(MemberID memberID) {
        this.memberID = memberID;
    }

    public ResponseWrapper(OrganizationDetail organizationDetail) {
        this.organizationDetail = organizationDetail;
    }

    public boolean isValid() {
        return isValid;
    }

    public MemberID getMemberID() {
        return memberID;
    }

    public OrganizationDetail getOrganizationDetail() {
        return organizationDetail;
    }

    public MemberPR getMemberPR() {
        return memberPR;
    }

    public HashMap<String, Member> getMembers() {
        return members;
    }

    public Repositories getRepositories() {
        return repositories;
    }

    public Teams getTeams() {
        return teams;
    }

    public HashMap<String, ArrayList<Calendar>> getComittedRepos() {
        return comittedRepos;
    }

    public HashMap<String, ArrayList<Calendar>> getPullRequestsDates() {
        return pullRequestsDates;
    }

    public CreatedRepositoriesByMembers getCreatedRepositoriesByMembers() {
        return createdRepositoriesByMembers;
    }
}
