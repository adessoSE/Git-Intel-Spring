package de.adesso.gitstalker.core.processors;

import de.adesso.gitstalker.core.enums.RequestType;
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

import java.util.ArrayList;
import java.util.HashMap;

public class CreatedReposByMembersProcessor extends ResponseProcessor {

    private RequestRepository requestRepository;
    private OrganizationRepository organizationRepository;
    private Query requestQuery;
    private OrganizationWrapper organization;
    private HashMap<String, ArrayList<Repository>> createdRepositoriesByMembers = new HashMap<>();


    public CreatedReposByMembersProcessor() {
    }

    protected void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(this.requestQuery.getOrganizationName());
    }

    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        Data response = ((ResponseCreatedReposByMembers) this.requestQuery.getQueryResponse()).getData();

        this.processQueryResponse(response);
        super.updateRateLimit(response.getRateLimit(), this.requestQuery.getQueryRequestType());
        this.processRemainingRepositoriesOfMember(response.getNode().getRepositories().getPageInfo(), this.requestQuery.getOrganizationName(), response.getNode().getId());
        this.checkIfRequestTypeIsFinished();
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.organization, this.requestQuery, RequestType.CREATED_REPOS_BY_MEMBERS);
    }

    protected void checkIfRequestTypeIsFinished() {
        if (super.checkIfQueryIsLastOfRequestType(this.organization, this.requestQuery, RequestType.CREATED_REPOS_BY_MEMBERS, this.requestRepository)) {
            this.organization.addCreatedReposByMembers(createdRepositoriesByMembers);
        }
    }

    protected void processRemainingRepositoriesOfMember(PageInfoRepositories pageInfo, String organizationName, String memberID) {
        if (pageInfo.hasNextPage()) {
            Query generatedNextQuery = new RequestManager(organizationName, memberID, pageInfo.getEndCursor()).generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS);
            this.requestRepository.save(generatedNextQuery);
        }
    }

    protected void processQueryResponse(Data response) {
        ArrayList<Repository> createdRepositoriesByMember = new ArrayList<>();

        for (NodesRepositories repository : response.getNode().getRepositories().getNodes()) {
            if (repository.isFork() || repository.isMirror() || !repository.getOwner().getId().equals(response.getNode().getId())) {
                continue;
            }

            int stars = repository.getStargazers().getTotalCount();
            int forks = repository.getForkCount();
            String url = repository.getUrl();
            String license = getLicense(repository);
            String programmingLanguage = getProgrammingLanguage(repository);
            String description = getDescription(repository);
            String name = repository.getName();

            createdRepositoriesByMember.add(new Repository(name, url, description, programmingLanguage, license, forks, stars));
        }
        if (checkIfMemberIsAlreadyAssigned(response.getNode().getId())) {
            this.createdRepositoriesByMembers.get(response.getNode().getId()).addAll(createdRepositoriesByMember);
        } else this.createdRepositoriesByMembers.put(response.getNode().getId(), createdRepositoriesByMember);
    }

    protected boolean checkIfMemberIsAlreadyAssigned(String memberID) {
        return this.createdRepositoriesByMembers.containsKey(memberID);
    }

    protected String getLicense(NodesRepositories repository) {
        if (repository.getLicenseInfo() == null) return "";
        else return repository.getLicenseInfo().getName();
    }

    protected String getProgrammingLanguage(NodesRepositories repository) {
        if (repository.getPrimaryLanguage() == null) return "";
        else return repository.getPrimaryLanguage().getName();
    }

    protected String getDescription(NodesRepositories repository) {
        if (repository.getDescription() == null) return "";
        else return repository.getDescription();
    }

    public void setCreatedRepositoriesByMembers(HashMap<String, ArrayList<Repository>> createdRepositoriesByMembers) {
        this.createdRepositoriesByMembers = createdRepositoriesByMembers;
    }

    public HashMap<String, ArrayList<Repository>> getCreatedRepositoriesByMembers() {
        return createdRepositoriesByMembers;
    }

    public RequestRepository getRequestRepository() {
        return requestRepository;
    }

    public OrganizationRepository getOrganizationRepository() {
        return organizationRepository;
    }

    public Query getRequestQuery() {
        return requestQuery;
    }

    public OrganizationWrapper getOrganization() {
        return organization;
    }
}
