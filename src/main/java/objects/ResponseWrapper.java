package objects;


public class ResponseWrapper {

    private Response response;
    private MemberID memberID;
    private OrganizationDetail organizationDetail;
    private RepositoryID repositoryID;
    private MemberPR memberPR;

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    public ResponseWrapper(MemberPR memberPR){ this.memberPR = memberPR;}

    public ResponseWrapper(MemberID memberID) {
        this.memberID = memberID;
    }

    public ResponseWrapper(RepositoryID repositoryID) {
        this.repositoryID = repositoryID;
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

    public RepositoryID getRepositoryID() {
        return repositoryID;
    }

    public MemberPR getMemberPR() {
        return memberPR;
    }


}
