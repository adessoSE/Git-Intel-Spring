package requests;

import config.Config;
import enums.RequestStatus;
import objects.Query;
import objects.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import resources.externalRepo_Resources.ResponseExternalRepository;
import resources.memberID_Resources.ResponseMemberID;
import resources.memberPR_Resources.ResponseMemberPR;
import resources.member_Resources.ResponseMember;
import resources.organisation_Resources.ResponseOrganization;
import resources.repository_Resources.ResponseRepository;
import resources.team_Resources.ResponseTeam;


public abstract class Request {


    /**
     * Starting of the request to the GraphQL GitHub API. Setting up the fundamental structure of the request and processing the request.
     *
     * @param requestQuery Query containing the content for the request.
     * @return Requested query is returned with the new status and response/error.
     */
    public Query crawlData(Query requestQuery) {
        requestQuery.setQueryStatus(RequestStatus.STARTED);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + Config.API_TOKEN);
        HttpEntity entity = new HttpEntity(requestQuery, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            processRequest(requestQuery, restTemplate, entity);
        } catch (HttpClientErrorException e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECEIVED);
            requestQuery.setQueryError(e.toString());
        } catch (Exception e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECEIVED);
            requestQuery.setQueryError(e.toString());
        }
        return requestQuery;
    }

    /**
     * Processing the request according to the request type. Differentiation needed because of the various response classes.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
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
            case MEMBER:
                processMemberRequest(requestQuery, restTemplate, entity);
                break;
            case REPOSITORY:
                processRespositoriesDetail(requestQuery, restTemplate, entity);
                break;
            case TEAM:
                processOrganizationTeams(requestQuery, restTemplate, entity);
                break;
            case EXTERNAL_REPO:
                processExternalRepos(requestQuery, restTemplate, entity);
                break;
        }
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED);
    }

    /**
     * Processing of the request for the external repos with contribution by the members. Usage of the Response class for the external repos.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processExternalRepos(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseExternalRepository.class)));
    }

    /**
     * Processing of the request for the teams detail of the organization. Usage of the Response class for the teams.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationTeams(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseTeam.class)));
    }

    /**
     * Processing of the request for the repositories detail of the organization. Usage of the Response class for the repositories.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processRespositoriesDetail(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseRepository.class)));
    }

    /**
     * Processing of the request for the organization detail. Usage of the Response class for the organization.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseOrganization.class)));
    }

    /**
     * Processing of the request for the memberID. Usage of the Response class for the memberID.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberIDRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMemberID.class)));
    }

    /**
     * Processing of the request for the memberPRRepos. Usage of the Response class for the MemberPR.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberPRRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMemberPR.class)));
    }


    /**
     * Processing of the request for the memberPRRepos. Usage of the Response class for the MemberPR.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        requestQuery.setQueryResponse(new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMember.class)));
    }
}
