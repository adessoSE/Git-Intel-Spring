package de.adesso.gitstalker.core.requests;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.exceptions.InvalidGithubAPITokenException;
import de.adesso.gitstalker.core.exceptions.InvalidRequestContentException;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import de.adesso.gitstalker.core.resources.createdReposByMembers.ResponseCreatedReposByMembers;
import de.adesso.gitstalker.core.resources.externalRepo_Resources.ResponseExternalRepository;
import de.adesso.gitstalker.core.resources.memberID_Resources.ResponseMemberID;
import de.adesso.gitstalker.core.resources.memberPR_Resources.ResponseMemberPR;
import de.adesso.gitstalker.core.resources.member_Resources.ResponseMember;
import de.adesso.gitstalker.core.resources.organisation_Resources.ResponseOrganization;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;
import de.adesso.gitstalker.core.resources.repository_Resources.ResponseRepository;
import de.adesso.gitstalker.core.resources.team_Resources.ResponseTeam;

import java.util.Objects;


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
    private void processRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        switch (requestQuery.getQueryRequestType()) {
            case ORGANIZATION_VALIDATION:
                processOrganizationValidationRequest(requestQuery, restTemplate, entity);
                break;
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
    }

    /**
     * Processing of the request for validating an organization's name.
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationValidationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        ResponseOrganizationValidation response = restTemplate.postForObject(Config.API_URL, entity, ResponseOrganizationValidation.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseCreatedReposByMembers response = restTemplate.postForObject(Config.API_URL, entity, ResponseCreatedReposByMembers.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseExternalRepository response = restTemplate.postForObject(Config.API_URL, entity, ResponseExternalRepository.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseTeam response = restTemplate.postForObject(Config.API_URL, entity, ResponseTeam.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
    }

    /**
     * Processing of the request for the de.adesso.gitstalker.core.repositories detail of the organization. Usage of the Response class for the de.adesso.gitstalker.core.repositories.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processRespositoriesDetail(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        ResponseRepository response = restTemplate.postForObject(Config.API_URL, entity, ResponseRepository.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
    }

    /**
     * Processing of the request for the organization detail. Usage of the Response class for the organization.
     * Throws NullPointerException if response data is null because of bad request.
     *
     * @param requestQuery Query used for the request
     * @param restTemplate RestTemplate created for the request
     * @param entity       Configuration for the request
     */
    private void processOrganizationRequest(Query requestQuery, RestTemplate restTemplate, HttpEntity entity) {
        ResponseOrganization response = restTemplate.postForObject(Config.API_URL, entity, ResponseOrganization.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseMemberID response = restTemplate.postForObject(Config.API_URL, entity, ResponseMemberID.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseMemberPR response = restTemplate.postForObject(Config.API_URL, entity, ResponseMemberPR.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
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
        ResponseMember response = restTemplate.postForObject(Config.API_URL, entity, ResponseMember.class);
        this.setQueryResponseInRequestQuery(requestQuery, response, Objects.nonNull(response.getData()));
    }

    private void setQueryResponseInRequestQuery(Query requestQuery, Response response, boolean isValidResponse){
        if (isValidResponse) {
            requestQuery.setQueryResponse(response);
            requestQuery.setQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED);
        } else throw new NullPointerException("Invalid request content: Returned response null!");
    }
}
