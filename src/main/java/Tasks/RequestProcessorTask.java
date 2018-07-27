package Tasks;

import enums.RequestStatus;
import enums.RequestType;
import objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repositories.RequestRepository;

import java.util.ArrayList;

public class RequestProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task checking for queries without crawled information.
     * After picking one query the request starts with the specified information out of the selected query. After the request the query is saved in the repository with the additional response data.
     */
    @Scheduled(fixedRate = 500)
    private void crawlQueryData() {
        ArrayList<Query> processingQuerys = requestRepository.findByQueryStatus(RequestStatus.CREATED);
        if (!processingQuerys.isEmpty()) {
            Query processingQuery = processingQuerys.get(0);
            requestRepository.delete(processingQuery);
            processingQuery.crawlQueryResponse();
            requestRepository.save(processingQuery);
        }
    }

}
