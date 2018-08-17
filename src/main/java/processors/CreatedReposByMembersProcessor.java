package processors;

import enums.RequestType;
import objects.OrganizationWrapper;
import objects.Query;
import objects.Repository;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;
import resources.createdReposByMembers.Data;
import resources.createdReposByMembers.NodesRepositories;
import resources.createdReposByMembers.PageInfoRepositories;

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

    private void setUp(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.requestQuery = requestQuery;
        this.requestRepository = requestRepository;
        this.organizationRepository = organizationRepository;
        this.organization = this.organizationRepository.findByOrganizationName(this.requestQuery.getOrganizationName());
    }

    public void processResponse(Query requestQuery, RequestRepository requestRepository, OrganizationRepository organizationRepository) {
        this.setUp(requestQuery, requestRepository, organizationRepository);
        Data response = this.requestQuery.getQueryResponse().getResponseCreatedReposByMembers().getData();

        this.processQueryResponse(response);
        super.updateRateLimit(response.getRateLimit(), this.requestQuery.getQueryRequestType());
        this.processRemainingRepositoriesOfMember(response.getNode().getRepositories().getPageInfo(), this.requestQuery.getOrganizationName(), response.getNode().getId());
        this.checkIfRequestTypeIsFinished();
        super.doFinishingQueryProcedure(this.requestRepository, this.organizationRepository, this.organization, this.requestQuery, RequestType.CREATED_REPOS_BY_MEMBERS);
    }

    //TODO: Hinzufügen gesamten Repos zum Schluss funktioniert nicht
    private void checkIfRequestTypeIsFinished(){
        System.out.println("Check Last Request: " + super.checkIfRequestTypeIsFinished(this.organization, RequestType.CREATED_REPOS_BY_MEMBERS));
        if(super.checkIfRequestTypeIsFinished(this.organization, RequestType.CREATED_REPOS_BY_MEMBERS)){
            this.organization.addCreatedReposByMembers(createdRepositoriesByMembers);
        }
    }
    private void processRemainingRepositoriesOfMember(PageInfoRepositories pageInfo, String organizationName, String memberID) {
        if (pageInfo.hasNextPage()) {
            this.requestRepository.save(new RequestManager(organizationName, memberID, pageInfo.getEndCursor()).generateRequest(RequestType.CREATED_REPOS_BY_MEMBERS));
        }
    }
//TODO: Prüfung wenn Nutzer schon da ist Repos hinzufügen
    private void processQueryResponse(Data response) {
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
        this.createdRepositoriesByMembers.put(response.getNode().getId(), createdRepositoriesByMember);
    }

    private String getLicense(NodesRepositories repository) {
        if (repository.getLicenseInfo() == null) return "";
        else return repository.getLicenseInfo().getName();
    }

    private String getProgrammingLanguage(NodesRepositories repository) {
        if (repository.getPrimaryLanguage() == null) return "";
        else return repository.getPrimaryLanguage().getName();
    }

    private String getDescription(NodesRepositories repository) {
        if (repository.getDescription() == null) return "";
        else return repository.getDescription();
    }
}
