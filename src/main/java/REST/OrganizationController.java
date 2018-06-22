package REST;


import objects.ChartJSData;
import objects.Member;
import objects.OrganizationDetail;
import objects.OrganizationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrganizationController {

    public enum Type {COMMITS, ISSUES, PRS}

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RequestRepository requestRepository;

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
        if(this.checkIfDataAvailable(name)){
            return this.organizationRepository.findByOrganizationName(name).getOrganizationDetail();
        } else return null;
    }

    @RequestMapping("/organizationobject")
    public OrganizationWrapper retrieveOrganizationObject(@RequestParam(value = "name") String name) {
        if(this.checkIfDataAvailable(name)){
            return this.organizationRepository.findByOrganizationName(name);
        } else return null;
    }

    @RequestMapping("/members")
    public ArrayList<Member> retrieveMembers(@RequestParam(value = "name") String name) {
        if(this.checkIfDataAvailable(name)){
            return this.organizationRepository.findByOrganizationName(name).getMembers();
        } else return null;
    }

    /**
     * Method used to check if there is already requested information available. If there are no requests running for the requested organization then the requests are generated.
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
}
