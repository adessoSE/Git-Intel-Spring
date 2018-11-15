package de.adesso.gitstalker.core.repositories;

import de.adesso.gitstalker.core.enums.RequestStatus;
import de.adesso.gitstalker.core.enums.RequestType;
import de.adesso.gitstalker.core.objects.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * MongoRepository for storing queries before processing in the different tasks.
 */
public interface RequestRepository extends MongoRepository<Query, String> {

    ArrayList<Query> findByOrganizationName(String organizationName);

    ArrayList<Query> findByQueryStatus(RequestStatus status);

    ArrayList<Query> findByQueryRequestTypeAndOrganizationName(RequestType requestType, String organizationName);

    ArrayList<Query> findByQueryStatusAndOrganizationName(RequestStatus status, String organizationName);
}
