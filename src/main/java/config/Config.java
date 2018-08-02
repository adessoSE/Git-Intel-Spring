package config;

public class Config {

    /**
     * Github API Token is used to authorize the request. Without token the request will be signed as unauthorized and won't receive a response.
     * Github API Url is used to specify the interface where the request needs to be posted.
     */

    // API Configuration
    public static final String API_TOKEN = "INSERT_API_TOKEN";
    public static final String API_URL = "https://api.github.com/graphql";

    public static final int PAST_DAYS_AMOUNT_TO_CRAWL = 7;
}
