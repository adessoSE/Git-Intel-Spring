package processors;

import objects.Member;
import objects.MemberID;
import objects.Query;
import objects.ResponseWrapper;

import java.util.ArrayList;

public class MemberProcessor {

    private Query requestQuery;

    public MemberProcessor(Query requestQuery) {
        this.requestQuery = requestQuery;
    }

    public ResponseWrapper processResponse() {
        ArrayList<Member> memberIDs = new ArrayList<>();
        System.out.println(this.requestQuery.getQueryResponse().getNodes().get(0).getName());
        return new ResponseWrapper(this.requestQuery.getQueryResponse());
    }
}
