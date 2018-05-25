package Tasks;

import enums.RequestStatus;
import objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import repositories.RequestRepository;

public class RequestProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    @Scheduled(fixedRate = 10000)
    private void crawlQueryData() {
        if (!requestRepository.findByQueryStatus(RequestStatus.CREATED).isEmpty()) {
            Query processingQuery = requestRepository.findByQueryStatus(RequestStatus.CREATED).get(0);
            requestRepository.delete(processingQuery);
            processingQuery.crawlQueryResponse();
            requestRepository.save(processingQuery);
        }
    }

}
