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

    @Scheduled(fixedRate = 2000)
    private void processCrawledQueryData() {
        if (!requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED).isEmpty()) {
            Query processingQuery = requestRepository.findByQueryStatus(RequestStatus.VALID_ANSWER_RECIEVED).get(0);
            ResponseWrapper response = new ResponseProcessorManager(processingQuery).processResponse();
            if(response.getMemberID() != null){
                System.out.println(response.getMemberID().getMemberIDs());
            }

            if(response.getOrganizationDetail() != null){
                System.out.println(response.getOrganizationDetail().getName());
            }

            requestRepository.delete(processingQuery);
        }
    }
}
