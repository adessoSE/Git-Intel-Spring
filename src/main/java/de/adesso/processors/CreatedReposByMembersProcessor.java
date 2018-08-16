package de.adesso.processors;

import de.adesso.objects.*;
import org.springframework.stereotype.Component;
import de.adesso.resources.createdReposByMembers.Data;
import de.adesso.resources.createdReposByMembers.NodesRepositories;
import de.adesso.resources.createdReposByMembers.PageInfoRepositories;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class CreatedReposByMembersProcessor extends ResponseProcessor{

    private Query processingQuery;

    public CreatedReposByMembersProcessor(Query processingQuery) {
        this.processingQuery = processingQuery;
    }

    public ResponseWrapper processResponse() {
        HashMap<String,ArrayList<Repository>> createdRepositoriesByMembers = new HashMap<>();
        ArrayList<Repository> createdRepositoriesByMember = new ArrayList<>();
        Data response = processingQuery.getQueryResponse().getResponseCreatedReposByMembers().getData();
        PageInfoRepositories pageInfo = response.getNode().getRepositories().getPageInfo();

        super.updateRateLimit(response.getRateLimit(), processingQuery.getQueryRequestType());

        for (NodesRepositories repository: response.getNode().getRepositories().getNodes()){
            if(repository.isFork() || repository.isMirror() || !repository.getOwner().getId().equals(response.getNode().getId())){
                continue;
            }

            int stars = repository.getStargazers().getTotalCount();
            int forks = repository.getForkCount();
            String url = repository.getUrl();
            String license = getLicense(repository);
            String programmingLanguage = getProgrammingLanguage(repository);
            String description = getDescription(repository);
            String name = repository.getName();

            createdRepositoriesByMember.add(new Repository(name,url,description,programmingLanguage,license,forks,stars));
        }
        createdRepositoriesByMembers.put(response.getNode().getId(), createdRepositoriesByMember);
        return new ResponseWrapper(new CreatedRepositoriesByMembers(createdRepositoriesByMembers,pageInfo.getEndCursor(),pageInfo.hasNextPage()));
    }

    private String getLicense(NodesRepositories repository) {
        if (repository.getLicenseInfo() == null) return "";
        else return repository.getLicenseInfo().getName();
    }

    private String getProgrammingLanguage(NodesRepositories repository) {
        if (repository.getPrimaryLanguage() == null) return "";
        else return repository.getPrimaryLanguage().getName();
    }

    private String getDescription(NodesRepositories repository) {
        if (repository.getDescription() == null) return "";
        el