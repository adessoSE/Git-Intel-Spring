package de.adesso.gitstalker.core.resources.rateLimit_Resources;

import java.util.Date;

public class RateLimit {

    private int cost;
    private int remaining;
    private Date resetAt;

    public int getCost() {
        return cost;
    }

    public int getRemaining() {
        return remaining;
    }

    public Date getResetAt() {
        return resetAt;
    }
}
