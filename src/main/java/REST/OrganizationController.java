package REST;


import enums.RequestType;
import objects.OrganizationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

@RestController
public class OrganizationController {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RequestRepository requestRepository;

    /**
     * Check if organization is already stored in data base. If not, initiate GitHub crawl for requested organization.
     *
     * TODO If organization exists, check if all requests are processed.
     * TODO Check if organization is valid before generating requests.
     *
     * @param name
     * @return
     */
    @RequestMapping("/organizations")
    public OrganizationWrapper organizations(@RequestParam(value = "name", defaultValue = "Google") String name) {
        if (organizationRepository.findByOrganizationName(name) == null && requestRepository.findByOrganizationName(name).isEmpty()) {
            requestRepository.saveAll(new RequestManager(name).generateAllRequests());
        } else {
            return this.organizationRepository.findByOrganizationName(name);
        }
        System.out.println("Response is null");
        return null;
    }
}
