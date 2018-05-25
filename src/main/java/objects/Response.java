package objects;

import resources.memberIDResources.ResponseMemberID;
import resources.organisationResources.ResponseOrganization;

public class Response {

    private ResponseMemberID responseMemberID;
    private ResponseOrganization responseOrganization;

    public Response() {}

    public Response(ResponseMemberID responseMemberID){
        this.responseMemberID = responseMemberID;
    }

    public Response(ResponseOrganization responseOrganization){
        this.responseOrganization = responseOrganization;
    }


    public ResponseMemberID getResponseMemberID() {
        return responseMemberID;
    }

    public ResponseOrganization getResponseOrganization() {
        return responseOrganization;
    }
}
