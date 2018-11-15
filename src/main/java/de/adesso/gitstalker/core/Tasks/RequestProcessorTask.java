package de.adesso.gitstalker.core.Tasks;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import de.adesso.gitstalker.core.config.Config;
import de.adesso.gitstalker.core.config.RateLimitConfig;
import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.exceptions.NoRemainingRateLimitException;
import de.adesso.gitstalker.core.objects.Query;
import de.adesso.gitstalker.core.resources.organization_validation.Organization;
import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import de.adesso.gitstalker.core.repositories.RequestRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestProcessorTask {

    @Autowired
    RequestRepository requestRepository;

    /**
     * Scheduled task checking for queries without crawled information.
     * After picking one query the request starts with the specified information out of the selected query. After the request the query is saved in the repository with the additional response data.
     */
    @Scheduled(fixedRate = Config.PROCESSING_RATE_IN_MS)
    private void crawlQueryData() {
        ArrayList<Query> queriesToProcess;
        if(!OrganizationController.processingOrganizations.isEmpty()){
            Map.Entry<String, ProcessingOrganization> processingOrganization = OrganizationController.processingOrganizations.entrySet().iterator().next();
            queriesToProcess = requestRepository.findByQueryStatusAndOrganizationName(RequestStatus.CREATED, processingOrganization.getKey());
        } else queriesToProcess = requestRepository.findByQueryStatus(RequestStatus.CREATED);

        if (!queriesToProcess.isEmpty() && RateLimitConfig.getRemainingRateLimit() != 0) {
            try {
                this.processQuery(this.findProcessableQueryByRequestCost(queriesToProcess));
            } catch (NoRemainingRateLimitException e) {
                e.printStackTrace();
            }
        }
    }

    private Query findProcessableQueryByRequestCost(ArrayList<Query> processingQueries) throws NoRemainingRateLimitException {
        for (Query createdQuery : processingQueries) {
            if (RateLimitConfig.getRemainingRateLimit() - createdQuery.getEstimatedQueryCost() >= 0) {
                return createdQuery;
            }
        }
        throw new NoRemainingRateLimitException("Rate Limit exhausted. Processing is paused until " + RateLimitConfig.getRemainingRateLimit());
    }

    private void processQuery(Query queryToProcess) {
            requestRepository.delete(queryToProcess);
            queryToProcess.crawlQueryResponse();
            requestRepository.save(queryToProcess);
    }
}
