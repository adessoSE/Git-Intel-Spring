package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.config.RateLimitConfig;
import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.gitstalker.core.repositories.RequestRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RequestProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task checking for queries without crawled information.
     * After picking one query the request starts with the specified information out of the selected query. After the request the query is saved in the repository with the additional response data.
     */
    @Scheduled(fixedRate = Config.PROCESSING_RATE_IN_MS)
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
            requestRepository.delete(queryToProcess);
            queryToProcess.crawlQueryResponse();
            requestRepository.save(queryToProcess);
    }
}
