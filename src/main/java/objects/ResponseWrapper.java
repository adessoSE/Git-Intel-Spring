package objects;

import entities.Level1.Data;

import java.util.ArrayList;

public class ResponseWrapper {

    private Data data;
    private MemberID memberID;
    private OrganizationDetail organizationDetail;
    private RepositoryID repositoryID;

    public ResponseWrapper(MemberID memberID) {
        this.memberID = memberID;
    }

    public ResponseWrapper(RepositoryID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public ResponseWrapper(OrganizationDetail organizationDetail){
        this.organizationDetail = organizationDetail;
    }

    public ResponseWrapper(Data data) {
        this.data = data;
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

    public Data getData() {
        return data;
    }

}
