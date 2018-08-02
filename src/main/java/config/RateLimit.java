package config;

import java.util.Date;

public class RateLimit {

    public static final int RATE_LIMIT = 5000;
    public static int REMAINING_RATE_LIMIT;
    public static Date RESET_RATE_LIMIT_AT;
}
