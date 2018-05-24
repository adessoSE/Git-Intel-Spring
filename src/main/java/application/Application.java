package application;

import enums.RequestStatus;
import enums.RequestType;
import objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import processors.ResponseProcessorManager;
import repositories.OrganizationRepository;
import repositories.RequestRepository;
import requests.RequestManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
@EnableMongoRepositories("repositories")
@EnableScheduling
public class Application implements CommandLineRunner {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private RequestRepository requestRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

    }

    /**
     * Scheduled to be executed in certain time intervals as to not overload the GitHub API endpoint.
     * Uses a RESTTemplate to send a GraphQL query to the GitHub V4 API endpoint and saves relevant response data in repositories.
     */
    @Scheduled(fixedRate = 2000)
    private void crawlQueryData() {
        if (!requestRepository.findByRequestStatus(RequestStatus.CREATED).isEmpty()) {
            System.out.println("Wir beginnen!");
            Query processingQuery = requestRepository.findByRequestStatus(RequestStatus.CREATED).get(0);
            processingQuery.setQueryStatus(RequestStatus.STARTED);
        }
    }

    @Scheduled(fixedRate = 2000)
    private void processCrawledQueryData() {

    }

    @Scheduled(fixedRate = 12000)
    private void generateQuery() {
        requestRepository.deleteAll();
        requestRepository.save(new RequestManager("microsoft").processRequest(RequestType.MEMBER_ID));
    }

    private void processMemberInformationQuerys(ArrayList<Query> querys){
        System.out.println(("Query zum durchlaufen: " + querys.size()));
        for(Query query : querys){
            new ResponseProcessorManager(query).processResponse();
        }
    }

    private ArrayList<Query> createMemberInformationQuerys(String organizationName, ArrayList<String> memberIDs) {
        ArrayList<Query> memberInformationQuerys = new ArrayList<>();
        while(!memberIDs.isEmpty()){
            System.out.println(memberInformationQuerys.size());
            memberInformationQuerys.add(new RequestManager(organizationName, memberIDs.subList(0,9)).processRequest(RequestType.MEMBER));
            memberIDs.removeAll(memberIDs.subList(0,9));
        }
        System.out.println("Größe Querys: " + memberInformationQuerys.size());
        return memberInformationQuerys;
    }

    private ArrayList<String> processMemberIDs(Query query) {
        ArrayList<String> memberIDs = new ArrayList<>();
        MemberID response = new ResponseProcessorManager(query).processResponse().getMemberID();
        memberIDs.addAll(response.getMemberIDs());
        while (response.isHasNextPage()) {
            response = new ResponseProcessorManager(new RequestManager(query.getOrganizationName(), response.getEndCursor()).processRequest(RequestType.MEMBER_ID)).processResponse().getMemberID();
            memberIDs.addAll(response.getMemberIDs());
        }
        System.out.println("Größe MemberIDs: " + memberIDs.size());
        return memberIDs;
    }

    private OrganizationDetail processOrganizationDetail(Query query) {
        OrganizationDetail response = new ResponseProcessorManager(query).processResponse().getOrganizationDetail();
        response.setNumOfExternalRepoContributions(processOrganizationExternalRepoContributions(query.getOrganizationName()));
        return response;
    }

    private ArrayList<String> processOrganizationRepositoriesIDs(String organizationName){
        ArrayList<String> repositoryIDs = new ArrayList<>();
        RepositoryID response = new ResponseProcessorManager(new RequestManager(organizationName).processRequest(RequestType.REPOSITORY_ID)).processResponse().getRepositoryID();
        repositoryIDs.addAll(response.getRepositoryIDs());
        while (response.isHasNextPage()) {
            response = new ResponseProcessorManager(new RequestManager(organizationName, response.getEndCursor()).processRequest(RequestType.REPOSITORY_ID)).processResponse().getRepositoryID();
            repositoryIDs.addAll(response.getRepositoryIDs());
        }
        return repositoryIDs;
    }

    private ArrayList<String> processMemberContributedRepositoriesIDs(String organizationName){
        ArrayList<String> memberRepositoryIDs = new ArrayList<>();
        RepositoryID responseMemberRepos = new ResponseProcessorManager(new RequestManager(organizationName).processRequest(RequestType.MEMBER_PR)).processResponse().getRepositoryID();
        memberRepositoryIDs.addAll(responseMemberRepos.getRepositoryIDs());
        while (responseMemberRepos.isHasNextPage()) {
            responseMemberRepos = new ResponseProcessorManager(new RequestManager(organizationName, responseMemberRepos.getEndCursor()).processRequest(RequestType.REPOSITORY_ID)).processResponse().getRepositoryID();
            memberRepositoryIDs.addAll(responseMemberRepos.getRepositoryIDs());
        }
        return memberRepositoryIDs;
    }

    private int processOrganizationExternalRepoContributions(String organizationName) {
        ArrayList<String> repositoryIDs = processOrganizationRepositoriesIDs(organizationName);
        ArrayList<String> externalContributions = new ArrayList<>();
            for (String memberRepositoryID : processMemberContributedRepositoriesIDs(organizationName)) {
                    if (!repositoryIDs.contains(memberRepositoryID)) {
                        externalContributions.add(memberRepositoryID);
                    }
            }

        return externalContributions.size();
    }

}
