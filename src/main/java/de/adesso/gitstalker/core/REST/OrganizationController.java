package de.adesso.gitstalker.core.REST;


import de.adesso.gitstalker.core.REST.responses.InvalidOrganization;
import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.exceptions.InvalidOrganizationNameRequestException;
import de.adesso.gitstalker.core.exceptions.ProcessingOrganizationException;
import de.adesso.gitstalker.core.objects.*;
import de.adesso.gitstalker.core.repositories.OrganizationRepository;
import de.adesso.gitstalker.core.repositories.RequestRepository;
import de.adesso.gitstalker.core.requests.RequestManager;
import de.adesso.gitstalker.core.resources.organization_validation.Organization;
import de.adesso.gitstalker.core.resources.organization_validation.ResponseOrganizationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class OrganizationController {

    OrganizationRepository organizationRepository;
    RequestRepository requestRepository;
    HashMap<String, ProcessingOrganization> processingOrganizations;

    @Autowired
    public OrganizationController(OrganizationRepository organizationRepository, RequestRepository requestRepository) {
        this.organizationRepository = organizationRepository;
        this.requestRepository = requestRepository;
        this.processingOrganizations = new HashMap<>();
    }

    /**
     * Check if organization is already stored in data base. If not, initiate GitHub crawl for requested organization.
     *
     * @return
     */
    @RequestMapping("/organizationdetail/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationDetail(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.ORGANIZATION_DETAIL,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/members/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationMembers(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.MEMBER,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/repositories/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationRepositories(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.REPOSITORY,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/externalrepositories/{organizationName}")
    public ResponseEntity<?> retrieveExternalRepositories(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.EXTERNAL_REPO,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/teams/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationTeams(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.TEAM,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/createdreposbymembers/{organizationName}")
    public ResponseEntity<?> retrieveCreatedReposByOrganizationMembers(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        return this.processResponseEntity(RequestType.CREATED_REPOS_BY_MEMBERS,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    @RequestMapping("/**")
    public ResponseEntity<Object> response404Error() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<?> processResponseEntity(RequestType requestType, String organizationName, HttpStatus httpStatus) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException {
        String formattedName = this.formatInput(organizationName);
        OrganizationWrapper organization = this.organizationRepository.findByOrganizationName(formattedName);

        if (httpStatus.is2xxSuccessful()) {
            switch (requestType) {
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
        } else if (httpStatus.is1xxInformational()) {
            throw new ProcessingOrganizationException("The transferred organization is being processed.", formattedName);
        } else if (httpStatus.is4xxClientError()) {
            throw new InvalidOrganizationNameRequestException("The transferred organization name is incorrect.", formattedName);
        }
        return new ResponseEntity<>(httpStatus);
    }

    @ExceptionHandler(InvalidOrganizationNameRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public InvalidOrganization handleInvalidOrganizationNameRequestException(InvalidOrganizationNameRequestException e) {
        return new InvalidOrganization()
                .setSearchedOrganization(e.getSearchedOrganization())
                .setErrorMessage(e.getMessage());
    }

    @ExceptionHandler(ProcessingOrganizationException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProcessingOrganization handleProcessingOrganizationException(ProcessingOrganizationException e) {
        return processingOrganizations.get(e.getSearchedOrganization())
                .setProcessingMessage(e.getMessage());
    }

    /**
     * Method used to check if there is already requested information available.
     * If there are no requests running for the requested organization then the requests are generated, after validating if the transferred organization is valid.
     *
     * @param organizationName Transferred organization name
     * @return HttpStatus Status if there is data available (200 - OK), if the data is processed (202 - Accepted) or if the organization is invalid (400 - Bad Request)
     */
    private HttpStatus checkStatusOfRequestedInformation(String organizationName) {
        if (requestRepository.findByOrganizationName(organizationName).isEmpty()) {
            if (organizationRepository.findByOrganizationName(organizationName) != null) {
                this.deleteFinishedOrganizationProcessingInHashMap(organizationName);
                return HttpStatus.OK;
            } else return this.validateOrganization(organizationName);
        } else {
            System.out.println("Data is still being gathered for this organization...");
            this.updateProcessingOrganizationInformation(organizationName);
            return HttpStatus.PROCESSING;
        }
    }

    /**
     * Method to delete the OrganizationProcessing object after the data was requested successfully.
     */
    private void deleteFinishedOrganizationProcessingInHashMap(String organizationName) {
        this.processingOrganizations.remove(organizationName);
    }

    /**
     * Method to validate the organization.
     * If the transferred organization name is valid the processing is started by creating a ProcessingOrganization object and saving the validation query for processing. Then the methods return the HttpStatus 102 - Processing.
     * If the transferred organization name is invalid the HttpStatus 400 - Bad Request is returned.
     *
     * @param organizationName
     * @return
     */
    private HttpStatus validateOrganization(String organizationName) {
        System.out.println("Validating organization...");
        Query queryOrganizationValidation = this.getOrganizationValidationResponse(organizationName);
        ResponseOrganizationValidation responseOrganizationValidation = (ResponseOrganizationValidation) queryOrganizationValidation.getQueryResponse();
        if (this.checkIfOrganizationIsValid(responseOrganizationValidation)) {
            this.addProcessingOrganizationInformationIfMissingForTheOrganization(organizationName, responseOrganizationValidation);
            this.requestRepository.save(queryOrganizationValidation);
            return HttpStatus.PROCESSING;
        } else return HttpStatus.BAD_REQUEST;
    }

    private void updateProcessingOrganizationInformation(String organizationName){
        OrganizationWrapper organizationWrapper = this.organizationRepository.findByOrganizationName(organizationName);
        ProcessingOrganization processingOrganization = this.processingOrganizations.get(organizationName);
        processingOrganization.setFinishedRequestTypes(organizationWrapper.getFinishedRequests());
        processingOrganization.getMissingRequestTypes().removeAll(organizationWrapper.getFinishedRequests());
    }

    private void addProcessingOrganizationInformationIfMissingForTheOrganization(String organizationName, ResponseOrganizationValidation responseOrganizationValidation) {
        if (!processingOrganizations.containsKey(organizationName)) {
            this.processingOrganizations.put(organizationName, this.generateProcessingOrganizationInformation(responseOrganizationValidation));
        }
    }

    private ProcessingOrganization generateProcessingOrganizationInformation(ResponseOrganizationValidation responseOrganizationValidation) {
        Organization organization = responseOrganizationValidation.getData().getOrganization();

        ProcessingOrganization processingOrganizationInformation = new ProcessingOrganization()
                .setProcessingMessage("Currently the organization is still under processing.")
                .setSearchedOrganization(organization.getName())
                .setMissingRequestTypes(new HashSet<>(Arrays.asList(RequestType.values())))
                .setFinishedRequestTypes(new HashSet<>())
                .setTotalCountOfNeededRequests(this.calculateTotalCountOfNeededRequests(organization))
                .setTotalCountOfRequestTypes(RequestType.values().length);

        return processingOrganizationInformation;
    }

    private int calculateTotalCountOfNeededRequests(Organization organization) {
        int memberTotalCount = organization.getMembers().getTotalCount();
        int teamTotalCount = organization.getTeams().getTotalCount();
        int repositoriesTotalCount = organization.getRepositories().getTotalCount();

        int totalCountOfNeededRequests = (memberTotalCount / 100) +     //Member ID Requests
                (memberTotalCount / 100) +                              //Member PR Requests
                (memberTotalCount) +                                    //Member Requests
                (repositoriesTotalCount / 100) +                        //Repositories Requests
                (memberTotalCount / 2) +                                //External Repo Requests
                (memberTotalCount) +                                    //Created Repos By Members Requests
                (teamTotalCount / 50) +                                 //Team Requests
                (2);                                                    //Organization Detail & Validation Requests

        return totalCountOfNeededRequests;
    }

    private boolean checkIfOrganizationIsValid(ResponseOrganizationValidation responseOrganizationValidation) {
        return responseOrganizationValidation.getData().getOrganization() != null;
    }

    private Query getOrganizationValidationResponse(String organizationName) {
        Query validationQuery = new RequestManager(organizationName).generateRequest(RequestType.ORGANIZATION_VALIDATION);
        validationQuery.crawlQueryResponse();
        return validationQuery;
    }

    private String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
