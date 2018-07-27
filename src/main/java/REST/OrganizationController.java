package REST;


import objects.*;
import objects.Team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
     * TODO Check if organization is valid before generating requests.
     *
     * @param name
     * @return
     */
    @RequestMapping("/organizationdetail")
    public OrganizationDetail retrieveOrganizationDetail(@RequestParam(value = "name") String name) {
        String formattedName = this.formatInput(name);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getOrganizationDetail();
        } else return null;
    }

    @RequestMapping("/organizationobject")
    public OrganizationWrapper retrieveOrganizationObject(@RequestParam(value = "name") String name) {
        String formattedName = this.formatInput(name);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName);
        } else return null;
    }

    @RequestMapping("/members")
    public Collection<Member> retrieveOrganizationMembers(@RequestParam(value = "name") String name) {
        String formattedName = this.formatInput(name);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getMembers().values();
        } else return null;
    }

    @RequestMapping("/repositories")
    public Collection<Repository> retrieveOrganizationRepositories(@RequestParam(value = "name") String name) {
        String formattedName = this.formatInput(name);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getRepositories().values();
        } else return null;
    }

    @RequestMapping("/teams")
    public Collection<Team> retrieveOrganizationTeams(@RequestParam(value = "name") String name) {
        String formattedName = this.formatInput(name);
        if (this.checkIfDataAvailable(formattedName)) {
            return this.organizationRepository.findByOrganizationName(formattedName).getTeams().values();
        } else return null;
    }

    /**
     * Method used to check if there is already requested information available. If there are no requests running for the requested organization then the requests are generated.
     *
     * @param organizationName Request organization name
     * @return boolean if there is data available
     */
    private boolean checkIfDataAvailable(String organizationName) {
        if (requestRepository.findByOrganizationName(organizationName).isEmpty()) {
            if (organizationRepository.findByOrganizationName(organizationName) != null) {
                return true;
            } else this.gatherData(organizationName);
        } else {
            System.out.println("Data is still being gathered for this organization");
        }
        return false;
    }

    private void gatherData(String organizationName) {
        requestRepository.saveAll(new RequestManager(organizationName).generateAllRequests());
        System.out.println("Organization data is being gathered. Try again in a few moments");
    }


    private String formatInput (String input) {
        return input.replaceAll("\\s+","").toLowerCase();
    }
}
