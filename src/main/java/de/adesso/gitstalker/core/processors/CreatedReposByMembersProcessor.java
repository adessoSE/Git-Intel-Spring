package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Member;
import de.adesso.gitstalker.core.objects.OrganizationWrapper;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.objects.Repository;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.createdReposByMembers.Data;
import de.adesso.gitstalker.core.resources.createdReposByMembers.NodesRepositories;
import de.adesso.gitstalker.core.resources.createdReposByMembers.PageInfoRepositories;
import de.adesso.gitstalker.core.resources.createdReposByMembers.ResponseCreatedReposByMembers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This is the response processor used for CreatedReposByMembers Request.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreatedReposByMembersProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;

    private HashMap<String, ArrayList<Repository>> createdRepositoriesByMembers = new HashMap<>();


    /**
     * Setting up the necessary parameters for the response processing.
     * @param requestQuery Query to be processed.
     * @param requestRepository RequestRepository for accessing requests.
     * @param organizationRepository OrganizationRepository for accessing organization.
     */
    protected void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(this.requestQuery.getOrganizationName());
    }

    /**
     * Performs the complete processing of an answer.
     * @param requestQuery Query to be processed.
     * @param requestRepository RequestRepository for accessing requests.
     * @param organizationRepository OrganizationRepository for accessing organization.
     */
    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        Data response = ((ResponseCreatedReposByMembers) this.requestQuery.getQueryResponse()).getData();

        this.processQueryResponse(response);
        super.updateRateLimit(response.getRateLimit(), this.requestQuery.getQueryRequestType());
        this.processRemainingRepositoriesOfMember(response.getNode().getRepositories().getPageInfo(), this.requestQuery.getOrganizationName(), response.getNode().getId());
        this.checkIfRequestTypeIsFinished();
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.organization, this.requestQuery, RequestType.CREATED_REPOS_BY_MEMBERS);
    }

    /**
     * Checks whether the response processing was the last processing for the request types. If processing is complete, the collected information is saved.
     */
    protected void checkIfRequestTypeIsFinished() {
        if (super.checkIfQueryIsLastOfRequestType(this.organization, this.requestQuery, RequestType.CREATED_REPOS_BY_MEMBERS, this.requestRepository)) {
            this.organization.addCreatedReposByMembers(createdRepositoriesByMembers);
        }
    }

    /**
     * Creates the subsequent requests if it becomes clear during processing that information is still open in the section.
     * @param pageInfo Contains information required to define whether requests are still outstanding.
     * @param organizationName Organization name for creating the appropriate request
     * @param memberID MemberID for creating the appropriate request
     */
    protected void processRemainingRepositoriesOfMember(PageInfoRepositories pageInfo, String organizationName, String memberID) {
        if (pageInfo.isHasNextPage()) {
            Query generatedNextQuery = new RequestManager()
                    .setOrganizationName(organizationName)
                    .setMemberID(memberID)
                    .setEndCursor(pageInfo.getEndCursor())
                    .generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS);
            this.requestRepository.save(generatedNextQuery);
        }
    }

    /**
     * Processing the response given by the Github API to the request.
     * @param response
     */
    protected void processQueryResponse(Data response) {
        ArrayList<Repository> createdRepositoriesByMember = new ArrayList<>();

        for (NodesRepositories repository : response.getNode().getRepositories().getNodes()) {
            if (repository.isFork() || repository.isMirror() || !repository.getOwner().getId().equals(response.getNode().getId())) {
                continue;
            }

            ArrayList<Member> repositoryOwner = new ArrayList<>(Arrays.asList(this.organizationRepository.findByOrganizationName(this.requestQuery.getOrganizationName()).getMembers().get(repository.getOwner().getId())));

            createdRepositoriesByMember.add(new Repository()
                    .setName(repository.getName())
                    .setUrl(repository.getUrl())
                    .setDescription(getDescription(repository))
                    .setProgrammingLanguage(getProgrammingLanguage(repository))
                    .setLicense(getLicense(repository))
                    .setForks(repository.getForkCount())
                    .setStars(repository.getStargazers().getTotalCount())
                    .setContributors(repositoryOwner));
        }
        if (checkIfMemberIsAlreadyAssigned(response.getNode().getId())) {
            this.createdRepositoriesByMembers.get(response.getNode().getId()).addAll(createdRepositoriesByMember);
        } else this.createdRepositoriesByMembers.put(response.getNode().getId(), createdRepositoriesByMember);
    }

    /**
     * Check whether the processed member has already been processed once.
     * @param memberID MemberID of the processed member
     * @return Boolean whether the member has already been processed or not
     */
    protected boolean checkIfMemberIsAlreadyAssigned(String memberID) {
        return this.createdRepositoriesByMembers.containsKey(memberID);
    }

    protected String getLicense(NodesRepositories repository) {
        if (repository.getLicenseInfo() == null) return "No License deposited";
        else return repository.getLicenseInfo().getName();
    }

    protected String getProgrammingLanguage(NodesRepositories repository) {
        if (repository.getPrimaryLanguage() == null) return "/";
        else return repository.getPrimaryLanguage().getName();
    }

    protected String getDescription(NodesRepositories repository) {
        if (repository.getDescription() == null) return "No Description deposited";
        else return repository.getDescription();
    }
}
