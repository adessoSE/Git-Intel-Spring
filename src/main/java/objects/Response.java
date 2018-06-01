package objects;

import resources.memberID_Resources.ResponseMemberID;
import resources.memberPR_Resources.ResponseMemberPR;
import resources.organisation_Resources.ResponseOrganization;
import resources.repositoryID_Resources.ResponseOrganRepoID;

public class Response {

    private ResponseMemberID responseMemberID;
    private ResponseMemberPR responseMemberPR;
    private ResponseOrganization responseOrganization;
    private ResponseOrganRepoID responseOrganRepoID;

    public Response() {
    }

    public Response (ResponseOrganRepoID responseOrganRepoID) {
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

}
