package de.adesso.gitstalker.core.REST;


import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@CrossOrigin
public class OrganizationController {

    OrganizationRepository organizationRepository;
    RequestRepository requestRepository;

    @Autowired
    public OrganizationController(OrganizationRepository organizationRepository, RequestRepository requestRepository) {
        this.organizationRepository = organizationRepository;
        this.requestRepository = requestRepository;
    }

    /**
     * Check if organization is already stored in data base. If not, initiate GitHub crawl for requested organization.
     *
     * @return
     */
    @RequestMapping("/organizationdetail/{organizationName}")
    public ResponseEntity<OrganizationDetail> retrieveOrganizationDetail(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<OrganizationDetail>) this.processResponseEntity(RequestType.ORGANIZATION_DETAIL, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/members/{organizationName}")
    public ResponseEntity<Collection<Member>> retrieveOrganizationMembers(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<Member>>) this.processResponseEntity(RequestType.MEMBER, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/repositories/{organizationName}")
    public ResponseEntity<Collection<Repository>> retrieveOrganizationRepositories(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<Repository>>) this.processResponseEntity(RequestType.REPOSITORY, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/externalrepositories/{organizationName}")
    public ResponseEntity<Collection<Repository>> retrieveExternalRepositories(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<Repository>>) this.processResponseEntity(RequestType.EXTERNAL_REPO, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/teams/{organizationName}")
    public ResponseEntity<Collection<Team>> retrieveOrganizationTeams(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<Team>>) this.processResponseEntity(RequestType.TEAM, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/createdreposbymembers/{organizationName}")
    public ResponseEntity<Collection<ArrayList<Repository>>> retrieveCreatedReposByOrganizationMembers(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<ArrayList<Repository>>>) this.processResponseEntity(RequestType.CREATED_REPOS_BY_MEMBERS, organization, this.checkStatusOfRequestedInformation(formattedName));
    }

    @RequestMapping("/**")
    public ResponseEntity<Object> response404Error() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> processResponseEntity(RequestType requestType, OrganizationWrapper organization, HttpStatus httpStatus){
        if (httpStatus.is2xxSuccessful()) {
            switch (requestType){
                case TEAM:
                    Collection<Team> teams = organization.getTeams().values();
                    return new ResponseEntity<>(teams, httpStatus);
                case CREATED_REPOS_BY_MEMBERS:
                    Collection<ArrayList<Repository>> createdReposByMembers = organization.getCreatedReposByMembers().values();
                    return new ResponseEntity<>(createdReposByMembers, httpStatus);
                case EXTERNAL_REPO:
                    Collection<Repository> externalRepositories = organization.getExternalRepos().values();
                    return new ResponseEntity<>(externalRepositories, httpStatus);
                case REPOSITORY:
                    Collection<Repository> organizationRepositories = organization.getRepositories().values();
                    return new ResponseEntity<>(organizationRepositories, httpStatus);
                case MEMBER:
                    Collection<Member> organizationMember = organization.getMembers().values();
                    return new ResponseEntity<>(organizationMember, httpStatus);
                case ORGANIZATION_DETAIL:
                    OrganizationDetail organizationDetail = organization.getOrganizationDetail();
                    return new ResponseEntity<>(organizationDetail, httpStatus);
            }
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Method used to check if there is already requested information available. If there are no requests running for the requested organization then the de.adesso.gitstalker.core.de.adesso.gitstalker.requests are generated.
     *
     * @param organizationName Request organization name
     * @return boolean if there is data available
     */
    private HttpStatus checkStatusOfRequestedInformation(String organizationName) {
        if (requestRepository.findByOrganizationName(organizationName).isEmpty()) {
            if (organizationRepository.findByOrganizationName(organizationName) != null) {
                return HttpStatus.OK;
            } else return this.validateOrganization(organizationName);
        } else {
            System.out.println("Data is still being gathered for this organization...");
            return HttpStatus.PROCESSING;
        }
    }

    private HttpStatus validateOrganization(String organizationName) {
        System.out.println("Validating organization...");
        Query validationQuery = new RequestManager(organizationName).generateRequest(RequestType.ORGANIZATION_VALIDATION);
        validationQuery.crawlQueryResponse();
        this.requestRepository.save(validationQuery);
        if(((ResponseOrganizationValidation) validationQuery.getQueryResponse()).getData().getOrganization() != null){
            return HttpStatus.PROCESSING;
        } else return HttpStatus.BAD_REQUEST;
    }

    private String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
