package requests;

import entities.Response;
import enums.RequestStatus;
import objects.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class Request {

    private String query;


    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Response crawlData(Query requestQuery) {
        requestQuery.setQueryStatus(RequestStatus.STARTED);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "a6501bb4d69760e7bbb30f653d50fa9fe8d64b6e");
        HttpEntity entity = new HttpEntity(requestQuery, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("https://api.github.com/graphql", entity, Response.class);
    }

    public String getDateWeekAgoInISO8601UTC() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)));
    }
}
