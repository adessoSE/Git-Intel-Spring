package application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import repositories.OrganizationRepository;
import entities.Response;

@SpringBootApplication
@EnableMongoRepositories("repositories")
@EnableScheduling
public class Application implements CommandLineRunner {

    @Autowired
    private OrganizationRepository organizationRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    /**
     * Scheduled to be executed in certain time intervals as to not overload the GitHub API endpoint.
     * Uses a RESTTemplate to send a GraphQL query to the GitHub V4 API endpoint and saves relevant response data in repositories.
     */
    @Scheduled(fixedRate = 5000)
    private void pullData() {

        // Create a new request and set the query to get the desired result;
        Request request = new Request();
        //        request.setQuery("query {viewer{login}}");
        request.setQuery("query {\n" +
                "  organization(login:\"adessoag\") {\n" +
                " name \n" +
                "    members(first: 100) {\n" +
                "      totalCount\n" +
                "    }\n" +
                "  }\n" +
                "}");

        // Configure http headers for authentication.
        // Use RestTemplate to send headers and body, saved in an entity, to GitHub API endpoint.
        // Save response.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "892ab3a10daec6bd0f0882d950357f1e09b4dc18");
        HttpEntity entity = new HttpEntity(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        Response response = restTemplate.postForObject("https://api.github.com/graphql", entity, Response.class);

        // Clear repository to ensure unique entries and save an organization
        organizationRepository.deleteAll();
        organizationRepository.save(response.getData().getOrganization());

        // ------DEBUGGING------
        // Read data set from repository and print its contents as string.
        ObjectMapper mapper = new ObjectMapper();
        try

        {
            System.out.println(mapper.writeValueAsString(organizationRepository.findByName("adesso AG")));
        } catch (
                Exception e)

        {
            System.out.println(e);
        }
        // ------DEBUGGING------
    }
}
