package application;

import Tasks.OrganisationUpdateTask;
import Tasks.RequestProcessorTask;
import Tasks.ResponseProcessorTask;
import enums.RequestStatus;
import enums.RequestType;
import objects.Query;
import objects.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessorManager;
import repositories.RequestRepository;
import requests.RequestManager;

@SpringBootApplication
@EnableMongoRepositories("repositories")
@EnableScheduling
public class Application {

    @Autowired
    RequestRepository requestRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public OrganisationUpdateTask organisationUpdateTask() {
        return new OrganisationUpdateTask();
    }

    @Bean
    public RequestProcessorTask requestProcessorTask() {
        return new RequestProcessorTask();
    }

    @Bean
    public ResponseProcessorTask responseProcessorTask() {
        return new ResponseProcessorTask();
    }
}
