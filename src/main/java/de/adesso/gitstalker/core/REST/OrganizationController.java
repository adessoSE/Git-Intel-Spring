package de.adesso.gitstalker.core.REST;


import de.adesso.gitstalker.core.REST.responses.ErrorMessage;
import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.exceptions.InvalidGithubAPITokenException;
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

    private OrganizationRepository organizationRepository;
    private RequestRepository requestRepository;
    public static HashMap<String, ProcessingOrganization> processingOrganizations;

    @Autowired
    public OrganizationController(OrganizationRepository organizationRepository, RequestRepository requestRepository) {
        this.organizationRepository = organizationRepository;
        this.requestRepository = requestRepository;
        processingOrganizations = new LinkedHashMap<>();
    }

    /**
     * Interface for the OrganizationDetail object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/organizationdetail/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationDetail(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.ORGANIZATION_DETAIL,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the Organization members object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/members/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationMembers(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.MEMBER,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the Organization repositories object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/repositories/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationRepositories(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.REPOSITORY,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the Organization repositories object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/externalrepositories/{organizationName}")
    public ResponseEntity<?> retrieveExternalRepositories(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.EXTERNAL_REPO,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the Organization teams object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/teams/{organizationName}")
    public ResponseEntity<?> retrieveOrganizationTeams(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.TEAM,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the Created Repo by organisation members object. Checks the current status of the queries for the relevant organization and provides an appropriate response.
     * @param organizationName Transferred organization name
     * @return Replies either with the data, various errors or processing information
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    @RequestMapping("/createdreposbymembers/{organizationName}")
    public ResponseEntity<?> retrieveCreatedReposByOrganizationMembers(@PathVariable String organizationName) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
        return this.processResponseEntity(RequestType.CREATED_REPOS_BY_MEMBERS,
                organizationName,
                this.checkStatusOfRequestedInformation(this.formatInput(organizationName)));
    }

    /**
     * Interface for the remaining requests so that mapping errors are not displayed.
     * @return 404 Error
     */
    @RequestMapping("/**")
    public ResponseEntity<Object> response404Error() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Processes the response using the request type and the HTTP status.
     * @param requestType Request type from the interface.
     * @param organizationName Transferred organization name from the interface.
     * @param httpStatus Calculated HTTP status of requested information.
     * @return Returns a ResponseEntity. Can contain a response, an error, or processing information depending on the calculated HTTP status.
     * @throws InvalidOrganizationNameRequestException Error if an invalid organization name was passed
     * @throws ProcessingOrganizationException Error if the requested organization is still being processed
     * @throws InvalidGithubAPITokenException Error if the Github token in the back end is invalid
     */
    private ResponseEntity<?> processResponseEntity(RequestType requestType, String organizationName, HttpStatus httpStatus) throws InvalidOrganizationNameRequestException, ProcessingOrganizationException, InvalidGithubAPITokenException {
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
            switch (httpStatus) {
                case UNAUTHORIZED:
                    throw new InvalidGithubAPITokenException("The entered token in the back-end seems to be incorrect...", formattedName);
                case BAD_REQUEST:
                    throw new InvalidOrganizationNameRequestException("The transferred organization name is incorrect.", formattedName);
            }
        }
        return new ResponseEntity<>(httpStatus);
    }

    /**
     * Exception handler for an invalid Github API token.
     * @param e Transferred thrown exception
     * @return Returns an ErrorMessage
     */
    @ExceptionHandler(InvalidGithubAPITokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidGithubAPITokenException(InvalidGithubAPITokenException e) {
        return new ErrorMessage()
                .setSearchedOrganization(e.getSearchedOrganization())
                .setErrorMessage(e.getMessage())
                .setErrorName("Invalid Github Token");
    }

    /**
     * Exception handler for an invalid organization name in request.
     * @param e Transferred thrown exception
     * @return Returns an ErrorMessage
     */
    @ExceptionHandler(InvalidOrganizationNameRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidOrganizationNameRequestException(InvalidOrganizationNameRequestException e) {
        return new ErrorMessage()
                .setSearchedOrganization(e.getSearchedOrganization())
                .setErrorMessage(e.getMessage())
                .setErrorName("Invalid Organization");
    }

    /**
     * Exception handler for an still processing organization.
     * @param e Transferred thrown exception
     * @return Returns an ProcessingInformation
     */
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
                return HttpStatus.OK;
            } else return this.validateOrganization(organizationName);
        } else {
            System.out.println("Data is still being gathered for this organization...");
            this.updateProcessingOrganizationInformation(organizationName);
            return HttpStatus.PROCESSING;
        }
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
        Query queryOrganizationValidation = this.getOrganizationValidationResponse(organizationName);
        ResponseOrganizationValidation responseOrganizationValidation = (ResponseOrganizationValidation) queryOrganizationValidation.getQueryResponse();
        if (queryOrganizationValidation.getQueryStatus().equals(RequestStatus.ERROR_RECEIVED)){
            return HttpStatus.UNAUTHORIZED;
        }
        if (this.checkIfOrganizationIsValid(responseOrganizationValidation)) {
            this.addProcessingOrganizationInformationIfMissingForTheOrganization(organizationName, responseOrganizationValidation);
            this.requestRepository.save(queryOrganizationValidation);
            return HttpStatus.PROCESSING;
        } else return HttpStatus.BAD_REQUEST;
    }

    /**
     * Updating of processing information to display queue position, completed and open requests.
     * @param organizationName Transferred organization name at the interface
     */
    private void updateProcessingOrganizationInformation(String organizationName){
        OrganizationWrapper organizationWrapper = this.organizationRepository.findByOrganizationName(organizationName);
        ProcessingOrganization processingOrganization = processingOrganizations.get(organizationName);
        processingOrganization.setCurrentPositionInQueue(this.calculatePositionInLinkedHashMap(organizationName));
        if (organizationWrapper != null){
            processingOrganization.setFinishedRequestTypes(organizationWrapper.getFinishedRequests());
            processingOrganization.getMissingRequestTypes().removeAll(organizationWrapper.getFinishedRequests());
        }
    }

    /**
     * Required calculation to determine the position in the LinkedHashMap.
     * @param organizationName Organization name for which the position is to be determined.
     * @return Position of the organization in the queue.
     */
    private int calculatePositionInLinkedHashMap(String organizationName){
        int calculatedPosition = 0;
        for (String organizationKey : processingOrganizations.keySet()){
            calculatedPosition += 1;
            if (organizationKey.matches(organizationName)){
                break;
            }
        }
        return calculatedPosition;
    }

    /**
     * Creates a new processing object if no processing object exists yet.
     * @param organizationName Organization name that is used as unique key.
     * @param responseOrganizationValidation Validation information for creating the processing object.
     */
    private void addProcessingOrganizationInformationIfMissingForTheOrganization(String organizationName, ResponseOrganizationValidation responseOrganizationValidation) {
        if (!processingOrganizations.containsKey(organizationName)) {
            processingOrganizations.put(organizationName, this.generateProcessingOrganizationInformation(responseOrganizationValidation));
        }
    }

    /**
     * Generates the processing object from the information of the validation request.
     * @param responseOrganizationValidation Response of the validation request.
     * @return Matching processing object.
     */
    private ProcessingOrganization generateProcessingOrganizationInformation(ResponseOrganizationValidation responseOrganizationValidation) {
        Organization organization = responseOrganizationValidation.getData().getOrganization();

        return new ProcessingOrganization()
                .setProcessingMessage("Currently the organization is still under processing.")
                .setSearchedOrganization(organization.getName())
                .setMissingRequestTypes(new HashSet<>(Arrays.asList(RequestType.values())))
                .setFinishedRequestTypes(new HashSet<>())
                .setTotalCountOfNeededRequests(this.calculateTotalCountOfNeededRequests(organization))
                .setTotalCountOfRequestTypes(RequestType.values().length)
                .setCurrentPositionInQueue(this.calculatePositionInLinkedHashMap(organization.getName())+1);
    }

    /**
     * Calculation of the approximate number of requests required to complete the complete organisation request.
     * @param organization Organisation object from the validation request.
     * @return Amount of needed requests to finish the organisation.
     */
    private int calculateTotalCountOfNeededRequests(Organization organization) {
        int memberTotalCount = organization.getMembers().getTotalCount();
        int teamTotalCount = organization.getTeams().getTotalCount();
        int repositoriesTotalCount = organization.getRepositories().getTotalCount();

        return (memberTotalCount / 100) +                               //Member ID Requests
                (memberTotalCount / 100) +                              //Member PR Requests
                (memberTotalCount) +                                    //Member Requests
                (repositoriesTotalCount / 100) +                        //Repositories Requests
                (memberTotalCount / 2) +                                //External Repo Requests
                (memberTotalCount) +                                    //Created Repos By Members Requests
                (teamTotalCount / 50) +                                 //Team Requests
                (2);                                                    //Organization Detail / Validation
    }

    /**
     * Checks if the organization is valid by checking if information has been returned.
     * @param responseOrganizationValidation Response of the validation request.
     * @return Boolean whether valid or not
     */
    //TODO: Nullpointer exception if the token was not set or is invalid.
    private boolean checkIfOrganizationIsValid(ResponseOrganizationValidation responseOrganizationValidation) {
        return responseOrganizationValidation.getData().getOrganization() != null;
    }

    /**
     * Initializes the request for the organizations validation and executes the request.
     * @param organizationName Organization to be validated
     * @return Query that contains the answer to the validation.
     */
    private Query getOrganizationValidationResponse(String organizationName) {
        Query validationQuery = new RequestManager(organizationName).generateRequest(RequestType.ORGANIZATION_VALIDATION);
        validationQuery.crawlQueryResponse();
        return validationQuery;
    }

    /**
     * Formatting the input.
     * @param input Input to format.
     * @return Formatted input.
     */
    private String formatInput(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
}
