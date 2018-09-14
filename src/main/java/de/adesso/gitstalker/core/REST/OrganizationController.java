package de.adesso.gitstalker.core.REST;


import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.objects.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;

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
     * TODO Check if organization is valid before generating de.adesso.gitstalker.core.requests.
     *
     * @return
     */
    @RequestMapping("/organizationdetail/{organizationName}")
    public OrganizationDetail retrieveOrganizationDetail(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getOrganizationDetail();
        } else return null;
    }

    @RequestMapping("/organizationobject/{organizationName}")
    public OrganizationWrapper retrieveOrganizationObject(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName);
        } else return null;
    }

    @RequestMapping("/members/{organizationName}")
    public Collection<Member> retrieveOrganizationMembers(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getMembers().values();
        } else return null;
    }

    @RequestMapping("/de/adesso/gitstalker/core/repositories/{organizationName}")
    public Collection<Repository> retrieveOrganizationRepositories(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getRepositories().values();
        } else return null;
    }

    @RequestMapping("/externalrepositories/{organizationName}")
    public Collection<Repository> retrieveExternalRepositories(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getExternalRepos().values();
        } else return null;
    }

    @RequestMapping("/teams/{organizationName}")
    public Collection<Team> retrieveOrganizationTeams(@PathVariable String organizationName) {
        String formattedName = this.formatInput(organizationName);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getTeams().values();
        } else return null;
    }

    /**
     * Method used to check if there is already requested information available. If there are no de.adesso.gitstalker.core.requests running for the requested organization then the de.adesso.gitstalker.core.requests are generated.
     *
     * @param organizationName Request organization name
     * @return boolean if there is data available
     */
    private boolean checkIfDataAvailable(String organizationName) {
        if (requestRepository.findByOrganizationName(organizationName).isEmpty()) {
            if (organizationRepository.findByOrganizationName(organizationName) != null) {
                return true;
            } else this.validateOrganization(organizationName);
        } else {
            System.out.println("Data is still being gathered for this organization...");
        }
        return false;
    }

    private void validateOrganization(String organizationName) {
        System.out.println("Validating organization...");
        requestRepository.save(new RequestManager(organizationName).generateRequest(RequestType.ORGANIZATION_VALIDATION));
    }


    private String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
