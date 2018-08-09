package requests;

import config.Config;
import enums.RequestStatus;
import exceptions.InvalidGithubAPITokenException;
import exceptions.InvalidRequestContentException;
import objects.Query;
import objects.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import resources.createdReposByMembers.ResponseCreatedReposByMembers;
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
            if (e.getMessage().equals("401 Unauthorized")) {
                requestQuery.setQueryError(new InvalidGithubAPITokenException("Invalid Github API Token! Maybe token wasn't added?", e));
            }
        } catch (NullPointerException e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECEIVED);
            if (e.getMessage().equals("Invalid request content: Returned response null!")) {
                //TODO: Add NullPointer Exception
                requestQuery.setQueryError(new InvalidRequestContentException("The content of the request is invalid! The returned data is null."));
            }
        } catch (Exception e) {
            requestQuery.setQueryStatus(RequestStatus.ERROR_RECEIVED);
            requestQuery.setQueryError(e);
        }

        return requestQuery;
    }

    /**
     * Processing the request according to the request type. Differentiation needed because of the various response classes.
     * Can throw an NullpointerException or HttpClientErrorException
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) throws Exception {
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
            case CREATED_REPOS_BY_MEMBERS:
                processCreatedReposByMembers(requestQuery, restTemplate, entity);
                break;
        }
        requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED);
    }

    /**
     * Processing of the request for the external repos with contribution by the members. Usage of the Response class for the external repos.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processCreatedReposByMembers(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseCreatedReposByMembers.class));
        if (response.getResponseCreatedReposByMembers().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the external repos with contribution by the members. Usage of the Response class for the external repos.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processExternalRepos(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseExternalRepository.class));
        if (response.getResponseExternalRepository().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the teams detail of the organization. Usage of the Response class for the teams.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationTeams(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseTeam.class));
        if (response.getResponseTeam().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the repositories detail of the organization. Usage of the Response class for the repositories.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processRespositoriesDetail(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseRepository.class));
        if (response.getResponseRepository().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the organization detail. Usage of the Response class for the organization.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) throws Exception {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseOrganization.class));
        if (response.getResponseOrganization().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the memberID. Usage of the Response class for the memberID.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberIDRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMemberID.class));
        if (response.getResponseMemberID().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }

    /**
     * Processing of the request for the memberPRRepos. Usage of the Response class for the MemberPR.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberPRRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMemberPR.class));
        if (response.getResponseMemberPR().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }


    /**
     * Processing of the request for the memberPRRepos. Usage of the Response class for the MemberPR.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processMemberRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        Response response = new Response(restTemplate.postForObject(Config.API_URL, entity, ResponseMember.class));
        if (response.getResponseMember().getData() != null) {
            requestQuery.setQueryResponse(response);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }
}
