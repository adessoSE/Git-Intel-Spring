package requests;

import objects.Response;
import resources.memberIDResources.ResponseMemberID;
import resources.organisationResources.ResponseOrganization;
import enums.RequestStatus;
import objects.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


public abstract class Request {

    private String query;
    private static final String API_TOKEN = "a6501bb4d69760e7bbb30f653d50fa9fe8d64b6e";
    private static final String API_URL = "https://api.github.com/graphql";


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Query crawlData(Query requestQuery) {
        requestQuery.setQueryStatus(RequestStatus.STARTED);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + API_TOKEN);
        HttpEntity entity = new HttpEntity(requestQuery, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            processRequest(requestQuery,restTemplate,entity);
        } catch (HttpClientErrorException e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECIEVED);
            requestQuery.setQueryError(e.toString());
        } catch (Exception e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECIEVED);
            requestQuery.setQueryError(e.toString());
        }
        return requestQuery;
    }

    private void processRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        switch (requestQuery.getQueryRequestType()) {
            case ORGANIZATION_DETAIL:
                processOrganizationRequest(requestQuery, restTemplate, entity);
                break;
            case MEMBER_ID:
                processMemberIDRequest(requestQuery, restTemplate, entity);
                break;
        }
    }

    private void processOrganizationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity){
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(API_URL, entity, ResponseOrganization.class)));
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED);
    }

    private void processMemberIDRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity){
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(API_URL, entity, ResponseMemberID.class)));
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED);
    }
}
