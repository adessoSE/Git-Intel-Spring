package de.adesso.gitstalker;

import de.adesso.gitstalker.core.REST.OrganizationController;
import de.adesso.gitstalker.core.Tasks.OrganizationUpdateTask;
import de.adesso.gitstalker.core.Tasks.RequestProcessorTask;
import de.adesso.gitstalker.core.Tasks.ResponseProcessorTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import de.adesso.gitstalker.core.repositories.RequestRepository;

@SpringBootApplication
@EnableMongoRepositories("de/adesso/gitstalker/core/repositories")
@EnableScheduling
@ComponentScan(basePackageClasses = OrganizationController.class)
public class Application {

    @Autowired
    RequestRepository requestRepository;

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
        return new OrganizationUpdateTask();
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
