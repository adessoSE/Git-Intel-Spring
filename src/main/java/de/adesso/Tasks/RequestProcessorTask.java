package de.adesso.Tasks;

import de.adesso.config.RateLimitConfig;
import de.adesso.enums.RequestStatus;
import de.adesso.objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.repositories.RequestRepository;

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
        ArrayList<Query> queriesToProcess = requestRepository.findByQueryStatus(RequestStatus.CREATED);

        if (!queriesToProcess.isEmpty() && RateLimitConfig.getRemainingRateLimit() != 0) {
            Query queryToProcess = this.findProcessableQueryByRequestCost(queriesToProcess);
            this.processQuery(queryToProcess);
        }
    }

    private Query findProcessableQueryByRequestCost(ArrayList<Query> processingQuerys) {
        for (Query createdQuery : processingQuerys) {
            if (RateLimitConfig.getRemainingRateLimit() - createdQuery.getEstimatedQueryCost() >= 0) {
                return createdQuery;
            }
        }
        return null;
    }

    private void processQuery(Query queryToProcess) {
        if (queryToProcess != null) {
            requestRepository.delete(queryToProcess);
            queryToProcess.crawlQueryResponse();
            requestRepository.save(queryToProcess);
        }
    }
}
