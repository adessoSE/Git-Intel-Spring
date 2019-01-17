package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.ProcessingRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import lombok.NoArgsConstructor;

import java.util.HashMap;

//TODO: Still relevant?
/**
 * This is the Response manager that carries the different processors within itself.
 * A separate processor is created for each organization.
 */
@NoArgsConstructor
public class ResponseProcessorManager {

    public static HashMap<String, OrganizationValidationProcessor> organizationValidationProcessorHashMap = new HashMap<>();
    public static HashMap<String, OrganizationDetailProcessor> organizationDetailProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberIDProcessor> memberIDProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberProcessor> memberProcessorHashMap = new HashMap<>();
    public static HashMap<String, MemberPRProcessor> memberPRProcessorHashMap = new HashMap<>();
    public static HashMap<String, RepositoryProcessor> repositoryProcessorHashMap = new HashMap<>();
    public static HashMap<String, TeamProcessor> teamProcessorHashMap = new HashMap<>();
    public static HashMap<String, ExternalRepoProcessor> externalRepoProcessorHashMap = new HashMap<>();
    public static HashMap<String, CreatedReposByMembersProcessor> createdReposByMembersProcessorHashMap = new HashMap<>();

    /**
     * The central routing of requests to processors is controlled here.
     * A separate processor is created for each organization so that no information can be mixed.
     * The processors are held as long as the organization is still being processed.
     * The processors are removed on completion.
     * @param organizationRepository OrganizationRepository for accessing organization.
     * @param requestRepository RequestRepository for accessing requests.
     * @param requestQuery Query to be processed.
     */
    public void processResponse(OrganizationRepository organizationRepository, RequestRepository requestRepository, ProcessingRepository processingRepository, Query requestQuery) {
        RequestType requestType = requestQuery.getQueryRequestType();
        switch (requestType) {
            case ORGANIZATION_VALIDATION:
                if (!organizationValidationProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    organizationValidationProcessorHashMap.put(requestQuery.getOrganizationName(), new OrganizationValidationProcessor(requestRepository, organizationRepository, processingRepository));
                }
                organizationValidationProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case ORGANIZATION_DETAIL:
                if (!organizationDetailProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    organizationDetailProcessorHashMap.put(requestQuery.getOrganizationName(), new OrganizationDetailProcessor(requestRepository, organizationRepository, processingRepository));
                }
                organizationDetailProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case MEMBER_ID:
                if (!memberIDProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberIDProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberIDProcessor(requestRepository, organizationRepository, processingRepository));
                }
                memberIDProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case MEMBER:
                if (!memberProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberProcessor(requestRepository, organizationRepository, processingRepository));
                }
                memberProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case MEMBER_PR:
                if (!memberPRProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    memberPRProcessorHashMap.put(requestQuery.getOrganizationName(), new MemberPRProcessor(requestRepository, organizationRepository, processingRepository));
                }
                memberPRProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case REPOSITORY:
                if (!repositoryProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    repositoryProcessorHashMap.put(requestQuery.getOrganizationName(), new RepositoryProcessor(requestRepository, organizationRepository, processingRepository));
                }
                repositoryProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case TEAM:
                if (!teamProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    teamProcessorHashMap.put(requestQuery.getOrganizationName(), new TeamProcessor(requestRepository, organizationRepository, processingRepository));
                }
                teamProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case EXTERNAL_REPO:
                if (!externalRepoProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    externalRepoProcessorHashMap.put(requestQuery.getOrganizationName(), new ExternalRepoProcessor(requestRepository, organizationRepository, processingRepository));
                }
                externalRepoProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
            case CREATED_REPOS_BY_MEMBERS:
                if (!createdReposByMembersProcessorHashMap.containsKey(requestQuery.getOrganizationName())) {
                    createdReposByMembersProcessorHashMap.put(requestQuery.getOrganizationName(), new CreatedReposByMembersProcessor(requestRepository, organizationRepository, processingRepository));
                }
                createdReposByMembersProcessorHashMap.get(requestQuery.getOrganizationName()).processResponse(requestQuery);
                break;
        }
    }
}
