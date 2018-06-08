import REST.OrganizationController;
import Tasks.OrganizationUpdateTask;
import Tasks.RequestProcessorTask;
import Tasks.ResponseProcessorTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import repositories.RequestRepository;

@SpringBootApplication
@EnableMongoRepositories("repositories")
@EnableScheduling
@ComponentScan(basePackageClasses = OrganizationController.class)
public class Application {

    @Autowired
    RequestRepository requestRepository;

    String name = "adessoAG";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initialization of the Update Task for the saved organisation
     *
     * @return OrganizationUpdateTask
     */
    @Bean
    public OrganizationUpdateTask organisationUpdateTask() {
        return new OrganizationUpdateTask(this.name);
    }

    /**
     * Initialization of the Request Task for crawling the data according to the saved queries.
     *
     * @return RequestProcessorTask
     */
    @Bean
    public RequestProcessorTask requestProcessorTask() {
        return new RequestProcessorTask();
}

    /**
     * Initialization of the Response Task for processing the request response data of the queries.
     *
     * @return ResponseProcessorTask
     */
    @Bean
    public ResponseProcessorTask responseProcessorTask() {
        return new ResponseProcessorTask();
    }
}
