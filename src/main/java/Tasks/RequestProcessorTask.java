package Tasks;

import enums.RequestStatus;
import objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repositories.RequestRepository;

public class RequestProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task checking for queries without crawled information.
     * After picking one query the request starts with the specified information out of the selected query. After the request the query is saved in the repository with the additional response data.
     */
    @Scheduled(fixedRate = 5000)
    private void crawlQueryData() {
        if (!requestRepository.findByQueryStatus(RequestStatus.CREATED).isEmpty()) {
            Query processingQuery = requestRepository.findByQueryStatus(RequestStatus.CREATED).get(0);
            requestRepository.delete(processingQuery);
            processingQuery.crawlQueryResponse();
            requestRepository.save(processingQuery);
        }
    }

}
