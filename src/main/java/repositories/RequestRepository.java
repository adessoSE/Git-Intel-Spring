package repositories;

import enums.RequestStatus;
import enums.RequestType;
import objects.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * MongoRepository for storing queries before processing in the different tasks.
 */
public interface RequestRepository extends MongoRepository<Query, String> {

    ArrayList<Query> findByQueryStatus(RequestStatus status);
}