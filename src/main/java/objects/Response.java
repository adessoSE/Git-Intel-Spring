package objects;

import resources.externalRepo_Resources.ResponseExternalRepository;
import resources.memberID_Resources.ResponseMemberID;
import resources.memberPR_Resources.ResponseMemberPR;
import resources.member_Resources.ResponseMember;
import resources.organisation_Resources.ResponseOrganization;
import resources.repositoryID_Resources.ResponseOrganRepoID;
import resources.repository_Resources.ResponseRepository;
import resources.team_Resources.ResponseTeam;

public class Response {

    private ResponseMemberID responseMemberID;
    private ResponseMemberPR responseMemberPR;
    private ResponseOrganization responseOrganization;
    private ResponseOrganRepoID responseOrganRepoID;
    private ResponseMember responseMember;
    private ResponseRepository responseRepository;
    private ResponseTeam responseTeam;
    private ResponseExternalRepository responseExternalRepository;

    public Response() {}

    public Response (ResponseExternalRepository responseExternalRepository) { this.responseExternalRepository = responseExternalRepository; }

    public Response(ResponseTeam responseTeam){ this.responseTeam = responseTeam; }

    public Response(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response(ResponseMember responseMember) {
        this.responseMember = responseMember;
    }

    public Response(ResponseOrganRepoID responseOrganRepoID) {
        this.responseOrganRepoID = responseOrganRepoID;
    }

    public Response(ResponseMemberPR responseMemberPR) {
        this.responseMemberPR = responseMemberPR;
    }

    public Response(ResponseMemberID responseMemberID) {
        this.responseMemberID = responseMemberID;
    }

    public Response(ResponseOrganization responseOrganization) {
        this.responseOrganization = responseOrganization;
    }


    public ResponseTeam getResponseTeam() {
        return responseTeam;
    }

    public ResponseOrganRepoID getResponseOrganRepoID() {
        return responseOrganRepoID;
    }

    public ResponseMemberID getResponseMemberID() {
        return responseMemberID;
    }

    public ResponseOrganization getResponseOrganization() {
        return responseOrganization;
    }

    public ResponseMemberPR getResponseMemberPR() {
        return responseMemberPR;
    }

    public ResponseMember getResponseMember() {
        return responseMember;
    }

    public ResponseRepository getResponseRepository() {
        return responseRepository;
    }

    public ResponseExternalRepository getResponseExternalRepository() {
        return responseExternalRepository;
    }
}
