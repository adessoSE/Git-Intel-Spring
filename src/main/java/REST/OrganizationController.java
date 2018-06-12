package REST;


import objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.util.ArrayList;

@RestController
public class OrganizationController {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RequestRepository requestRepository;

    /**
     * Check if organization is already stored in data base. If not, initiate GitHub crawl for requested organization.
     * <p>
     * TODO If organization exists, check if all requests are processed.
     * TODO Check if organization is valid before generating requests.
     *
     * @param name
     * @return
     */
    @RequestMapping("/organization")
    public OrganizationDetail organization(@RequestParam(value = "name") String name) {
        if (organizationRepository.findByOrganizationName(name) == null && requestRepository.findByOrganizationName(name).isEmpty()) {
            requestRepository.saveAll(new RequestManager(name).generateAllRequests());
            System.out.println("Organization data is being gathered");
        } else if (requestRepository.findByOrganizationName(name).isEmpty()) {
            return this.organizationRepository.findByOrganizationName(name).getOrganizationDetail();
        }
        return null;
    }

    @RequestMapping("/members")
    public ArrayList<Member> members(@RequestParam(value = "name") String name) {
        if (requestRepository.findByOrganizationName(name).isEmpty()) {
            if (organizationRepository.findByOrganizationName(name) == null) {
                requestRepository.saveAll(new RequestManager(name).generateAllRequests());
                System.out.println("Organization data is being gathered");
            } else {
                return this.organizationRepository.findByOrganizationName(name).getMembers();
            }
        }
        return null;
    }
}
