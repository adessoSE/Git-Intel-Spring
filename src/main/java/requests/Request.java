package requests;

import enums.RequestStatus;
import objects.Query;
import objects.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import resources.memberID_Resources.ResponseMemberID;
import resources.memberPR_Resources.ResponseMemberPR;
import resources.organisation_Resources.ResponseOrganization;


public abstract class Request {

    private String query;
    private static final String API_TOKEN = "5257a152697783b568f78cea50c143a73158765e";
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
            processRequest(requestQuery, restTemplate, entity);
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
            case MEMBER_PR:
                processMemberPRRequest(requestQuery, restTemplate, entity);
                break;

        }
    }

    private void processOrganizationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(API_URL, entity, ResponseOrganization.class)));
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED);
    }

    private void processMemberIDRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(API_URL, entity, ResponseMemberID.class)));
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED);
    }

    private void processMemberPRRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(API_URL, entity, ResponseMemberPR.class)));
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED);
    }
}
