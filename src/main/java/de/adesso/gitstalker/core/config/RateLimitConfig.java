package de.adesso.gitstalker.core.config;

import de.adesso.gitstalker.core.Tasks.RequestProcessorTask;
import de.adesso.gitstalker.core.enums.RequestType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.util.Date;
import java.util.HashMap;

/**
 * Overview of the complete rate limit, which is checked before each request. After each query an update takes place.
 */
@Data
public class RateLimitConfig {

    @Transient
    private static Logger logger = LoggerFactory.getLogger(RateLimitConfig.class);

    public static final int RATE_LIMIT = 5000;

    @Getter @Setter
    public static int remainingRateLimit = RATE_LIMIT;
    @Getter @Setter
    public static Date resetRateLimitAt;
    @Getter
    public static HashMap<RequestType, Integer> previousRequestCostAndRequestType = new HashMap<>();
    @Getter
    public static boolean rateLimitExhausted = false;

    public static void addPreviousRequestCostAndRequestType(Integer requestCost, RequestType requestType) {
        RateLimitConfig.previousRequestCostAndRequestType.put(requestType,requestCost);
    }

    public static boolean checkIfRateLimitWillReset(){
        rateLimitExhausted = true;
        if(RateLimitConfig.getResetRateLimitAt().before(new Date())){
            remainingRateLimit = RATE_LIMIT;
            rateLimitExhausted = false;
            logger.info("Rate Limit was reset!");
            return true;
        }
        return false;
    }
}
