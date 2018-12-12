package de.adesso.gitstalker.core.config;

import de.adesso.gitstalker.core.enums.RequestType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

/**
 * Overview of the complete rate limit, which is checked before each request. After each query an update takes place.
 */
@Data
public class RateLimitConfig {

    public static final int RATE_LIMIT = 5000;

    @Getter @Setter
    public static int remainingRateLimit = RATE_LIMIT;
    @Getter @Setter
    public static Date resetRateLimitAt;
    @Getter
    public static HashMap<RequestType, Integer> previousRequestCostAndRequestType = new HashMap<>();

    public static void addPreviousRequestCostAndRequestType(Integer requestCost, RequestType requestType) {
        RateLimitConfig.previousRequestCostAndRequestType.put(requestType,requestCost);
    }
}
