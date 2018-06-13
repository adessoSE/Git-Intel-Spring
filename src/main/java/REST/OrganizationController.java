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
        if (organizationRepository.findByOrganizationName(name) == null && requestRepository.findByOrganizationName(name).isEmpty()) {
            this.gatherData(name);
        } else if (requestRepository.findByOrganizationName(name).isEmpty()) {
            return this.organizationRepository.findByOrganizationName(name).getOrganizationDetail();
        }
        return null;
    }

    @RequestMapping("/organizationobject")
    public OrganizationWrapper retrieveOrganizationObject(@RequestParam(value = "name") String name) {
        if (organizationRepository.findByOrganizationName(name) == null && requestRepository.findByOrganizationName(name).isEmpty()) {
            this.gatherData(name);
        } else if (requestRepository.findByOrganizationName(name).isEmpty()) {
            return this.organizationRepository.findByOrganizationName(name);
        }
        return null;
    }

    @RequestMapping("/members")
    public ArrayList<Member> retrieveMembers(@RequestParam(value = "name") String name) {
        if (requestRepository.findByOrganizationName(name).isEmpty()) {
            if (organizationRepository.findByOrganizationName(name) == null) {
                this.gatherData(name);
            } else {
                return this.organizationRepository.findByOrganizationName(name).getMembers();
            }
        }
        return null;
    }

    @RequestMapping("/commithistory")
    public Map<String, ArrayList<Integer>> retrieveCommitHistory(@RequestParam(value = "name") String name) {
        if (requestRepository.findByOrganizationName(name).isEmpty()) {
            if (organizationRepository.findByOrganizationName(name) == null) {
                this.gatherData(name);
            } else {
                return this.packageData(name, Type.COMMITS);
            }
        } else {
            System.out.println("Data is still being gathered for this organization");
        }
        return null;
    }

    @RequestMapping("/prhistory")
    public Map<String, ArrayList<Integer>> retrievePullRequestHistory(@RequestParam(value = "name") String name) {
        if (requestRepository.findByOrganizationName(name).isEmpty()) {
            if (organizationRepository.findByOrganizationName(name) == null) {
                this.gatherData(name);
            } else {
                return this.packageData(name, Type.PRS);
            }
        } else {
            System.out.println("Data is still being gathered for this organization");
        }
        return null;
    }

    @RequestMapping("/issuehistory")
    public Map<String, ArrayList<Integer>> retrieveIssueHistory(@RequestParam(value = "name") String name) {
        if (requestRepository.findByOrganizationName(name).isEmpty()) {
            if (organizationRepository.findByOrganizationName(name) == null) {
                this.gatherData(name);
            } else {
                return this.packageData(name, Type.ISSUES);
            }
        } else {
            System.out.println("Data is still being gathered for this organization");
        }
        return null;
    }

    public void gatherData(String organizationName) {
        requestRepository.saveAll(new RequestManager(organizationName).generateAllRequests());
        System.out.println("Organization data is being gathered. Try again in a few moments");
    }

    /**
     * Pick commit, issue or pull reuquest data according to passed on parameter.
     * Package data as map with dates/labels as keys and commits/issues/prs as values - {Date-of-commit, [value1, value2, ...]}
     * @param organizationName
     * @param type
     * @return
     */
    public Map<String, ArrayList<Integer>> packageData(String organizationName, Type type) {
        Map<String, ArrayList<Integer>> data = new HashMap<>();
        for (Member member : organizationRepository.findByOrganizationName(organizationName).getMembers()) {
            ChartJSData memberData;
            switch (type) {
                case COMMITS: {
                    memberData = member.getPreviousCommits();
                    break;
                }
                case PRS: {
                    memberData = member.getPreviousPullRequests();
                    break;
                }
                case ISSUES: {
                    memberData = member.getPreviousIssues();
                    break;
                }
                default: {
                    memberData = null;
                }
            }
            for (int i = 0; i < memberData.getChartJSLabels().size(); i++) {
                String key = memberData.getChartJSLabels().get(i);
                int value = memberData.getChartJSDataset().get(i);
                if (!data.containsKey(key)) {
                    data.put(key, new ArrayList<>(value));
                } else {
                    data.get(key).add(value);
                }
            }
        }
        return data;
    }
}
