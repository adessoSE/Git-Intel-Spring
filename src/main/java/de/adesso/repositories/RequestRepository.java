package de.adesso.repositories;

import de.adesso.enums.RequestStatus;
import de.adesso.enums.RequestType;
import de.adesso.objects.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * MongoRepository for storing queries before processing in the different tasks.
 */
@Repository
public interface RequestRepository extends MongoRepository<Query, String> {

    ArrayList<Query> findByOrganizationName(String organizationName);

    ArrayList<Query> findByQueryStatus(RequestStatus status);

    ArrayList<Query> findByQueryRequestTypeAndOrganizationName(RequestType requestType, String organizationName);
}
