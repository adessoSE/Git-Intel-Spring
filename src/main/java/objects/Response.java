package objects;

import resources.memberID_Resources.ResponseMemberID;
import resources.memberPR_Resources.ResponseMemberPR;
import resources.member_Resources.ResponseMember;
import resources.organisation_Resources.ResponseOrganization;
import resources.repositoryID_Resources.ResponseOrganRepoID;
import resources.repository_Resources.ResponseRepository;

public class Response {

    private ResponseMemberID responseMemberID;
    private ResponseMemberPR responseMemberPR;
    private ResponseOrganization responseOrganization;
    private ResponseOrganRepoID responseOrganRepoID;
    private ResponseMember responseMember;
    private ResponseRepository responseRepository;

    public Response() {
    }

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
}
