package de.adesso.gitstalker.core.config;

import de.adesso.gitstalker.core.enums.RequestType;

import java.util.Date;
import java.util.HashMap;

public class RateLimitConfig {

    public static final int RATE_LIMIT = 5000;
    public static int REMAINING_RATE_LIMIT = RATE_LIMIT;
    public static Date RESET_RATE_LIMIT_AT;
    public static HashMap<RequestType, Integer> previousRequestCostAndRequestType = new HashMap<>();

    public static int getRateLimit() {
        return RATE_LIMIT;
    }

    public static int getRemainingRateLimit() {
        return REMAINING_RATE_LIMIT;
    }

    public static void setRemainingRateLimit(int remainingRateLimit) {
        REMAINING_RATE_LIMIT = remainingRateLimit;
    }

    public static Date getResetRateLimitAt() {
        return RESET_RATE_LIMIT_AT;
    }

    public static void setResetRateLimitAt(Date resetRateLimitAt) {
        RESET_RATE_LIMIT_AT = resetRateLimitAt;
    }

    public static HashMap<RequestType, Integer> getPreviousRequestCostAndRequestType() {
        return previousRequestCostAndRequestType;
    }

    public static void addPreviousRequestCostAndRequestType(Integer requestCost, RequestType requestType) {
        RateLimitConfig.previousRequestCostAndRequestType.put(requestType,requestCost);
    }
}
