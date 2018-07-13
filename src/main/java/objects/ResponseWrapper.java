package objects;


import objects.Member.Member;
import objects.Member.MemberID;
import objects.Member.MemberPR;
import objects.Organization.OrganizationDetail;
import objects.Repository.Repositories;
import objects.Team.Teams;

import java.util.HashMap;

public class ResponseWrapper {

    private Response response;
    private MemberID memberID;
    private OrganizationDetail organizationDetail;
    private MemberPR memberPR;
    private HashMap<String, Member> members;
    private Repositories repositories;
    private Teams teams;

    // TODO: Builder Pattern anwenden! Mehrere Constructoren mit ArrayList nicht zulässig!
    public ResponseWrapper(Teams teams) {
        this.teams = teams;
    }

    public ResponseWrapper(Repositories repositories) {
        this.repositories = repositories;
    }

    public ResponseWrapper(HashMap<String, Member> members) {
        this.members = members;
    }

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public ResponseWrapper(MemberPR memberPR) {
        this.memberPR = memberPR;
    }

    public ResponseWrapper(MemberID memberID) {
        this.memberID = memberID;
    }

    public ResponseWrapper(OrganizationDetail organizationDetail) {
        this.organizationDetail = organizationDetail;
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
}
