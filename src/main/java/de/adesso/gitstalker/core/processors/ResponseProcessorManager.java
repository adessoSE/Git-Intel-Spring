package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;

import java.util.HashMap;

public class ResponseProcessorManager {

    private OrganizationRepository organizationRepository;
    private RequestRepository requestRepository;

    public static HashMap<String, OrganizationValidationProcessor> organizationValidationProcessorHashMap = new HashMap<>();
    public static HashMap<String, OrganizationDetailProcessor> organizationDetailProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberIDProcessor> memberIDProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberProcessor> memberProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberPRProcessor> memberPRProcessorHashMap = new HashMap<>();
    public static HashMap<String, RepositoryProcessor> repositoryProcessorHashMap = new HashMap<>();
    public static HashMap<String, TeamProcessor> teamProcessorHashMap = new HashMap<>();
    public static HashMap<String, ExternalRepoProcessor> externalRepoProcessorHashMap = new HashMap<>();
    public static HashMap<String, CreatedReposByMembersProcessor> createdReposByMembersProcessorHashMap = new HashMap<>();

    public ResponseProcessorManager() {
    }

    public void processResponse(OrganizationRepository organizationRepository, RequestRepository requestRepository, Query requestQuery) {
        RequestType requestType = requestQuery.getQueryRequestType();
        switch (requestType) {
            case ORGANIZATION_VALIDATION:
                if (!organizationValidationProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    organizationValidationProcessorHashMap.put(requestQuery.getOrganizationName(), new OrganizationValidationProcessor());
                }
                organizationValidationProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case ORGANIZATION_DETAIL:
                if (!organizationDetailProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    organizationDetailProcessorHashMap.put(requestQuery.getOrganizationName(), new OrganizationDetailProcessor());
                }
                organizationDetailProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case MEMBER_ID:
                if (!memberIDProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberIDProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberIDProcessor());
                }
                memberIDProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case MEMBER:
                if (!memberProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberProcessor());
                }
                memberProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case MEMBER_PR:
                if (!memberPRProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberPRProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberPRProcessor());
                }
                memberPRProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case REPOSITORY:
                if (!repositoryProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    repositoryProcessorHashMap.put(requestQuery.getOrganizationName(), new RepositoryProcessor());
                }
                repositoryProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case TEAM:
                if (!teamProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    teamProcessorHashMap.put(requestQuery.getOrganizationName(), new TeamProcessor());
                }
                teamProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case EXTERNAL_REPO:
                if (!externalRepoProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    externalRepoProcessorHashMap.put(requestQuery.getOrganizationName(), new ExternalRepoProcessor());
                }
                externalRepoProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
            case CREATED_REPOS_BY_MEMBERS:
                if (!createdReposByMembersProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    createdReposByMembersProcessorHashMap.put(requestQuery.getOrganizationName(), new CreatedReposByMembersProcessor());
                }
                createdReposByMembersProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery, requestRepository, organizationRepository);
                break;
        }
    }
}
