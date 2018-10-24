package de.adesso.gitstalker.core.REST;


import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
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
     * <p>
     *
     * @return
     */
//    @RequestMapping("/organizationdetail/{organizationName}")
//    public OrganizationDetail retrieveOrganizationDetail(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName).getOrganizationDetail();
//        } else return null;
//    }
//
//    @RequestMapping("/organizationobject/{organizationName}")
//    public OrganizationWrapper retrieveOrganizationObject(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName);
//        } else return null;
//    }
//
//    @RequestMapping("/members/{organizationName}")
//    public Collection<Member> retrieveOrganizationMembers(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName).getMembers().values();
//        } else return null;
//    }
//
//    @RequestMapping("/repositories/{organizationName}")
//    public Collection<Repository> retrieveOrganizationRepositories(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName).getRepositories().values();
//        } else return null;
//    }
//
//    @RequestMapping("/externalrepositories/{organizationName}")
//    public Collection<Repository> retrieveExternalRepositories(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName).getExternalRepos().values();
//        } else return null;
//    }

    @RequestMapping("/teams/{organizationName}")
    public ResponseEntity<Collection<Team>> retrieveOrganizationTeams(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        return (ResponseEntity<Collection<Team>>) this.processResponseEntity("Team", organization, this.checkStatusOfRequestedInformation(formattedName));
    }

//    @RequestMapping("/createdreposbymembers/{organizationName}")
//    public Collection<ArrayList<Repository>> retrieveCreatedReposByOrganizationMembers(@PathVariable String organizationName) {
//        String formattedName = this.formatInput(organizationName);
//        if (this.checkIfDataAvailable(formattedName)) {
//            return this.organizationRepository.findByOrganizationName(formattedName).getCreatedReposByMembers().values();
//        } else return null;
//    }

    @RequestMapping("/**")
    public ResponseEntity<Object> response404Error() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> processResponseEntity(String requestType, OrganizationWrapper organization, HttpStatus httpStatus){
        if (httpStatus.is2xxSuccessful()) {
            switch (requestType){
                case "Team":
                    Collection<Team> teams = organization.getTeams().values();
                    return new ResponseEntity<>(teams, httpStatus);
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
        if(validationQuery.getQueryResponse().getResponseOrganizationValidation().getData().getOrganization() != null){
            return HttpStatus.PROCESSING;
        } else return HttpStatus.BAD_REQUEST;
    }

    private String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
