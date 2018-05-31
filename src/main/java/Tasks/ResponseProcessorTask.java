package Tasks;

import enums.RequestStatus;
import objects.Query;
import objects.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessorManager;
import repositories.RequestRepository;

public class ResponseProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task for checking all the saved queries response status. If there is a valid response the response processing begins.
     * The selected query is processed in the ProcessorManager which selects the suitable response processor. After successful processing the query is deleted.
     */
    @Scheduled(fixedRate = 2000)
    private void processCrawledQueryData() {
        if (!requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED).isEmpty()) {
            Query processingQuery = requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECEIVED).get(0);
            ResponseWrapper response = new ResponseProcessorManager(processingQuery).processResponse();
            System.out.println(response);
            if (response.getMemberID() != null) {
                System.out.println(response.getMemberID().getMemberIDs());
            }
            if (response.getOrganizationDetail() != null) {
                System.out.println(response.getOrganizationDetail().getName());
            }
            if (response.getMemberPR() != null) {
                System.out.println(response.getMemberPR().getMemberPRrepositoryIDs());
            }
            processingQuery.setQueryStatus(RequestStatus.FINISHED);
            requestRepository.delete(processingQuery);
        }
    }
}
