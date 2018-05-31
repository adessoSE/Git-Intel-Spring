package repositories;

import enums.RequestStatus;
import objects.Query;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * MongoRepository for storing queries before processing in the different tasks.
 */
public interface RequestRepository extends MongoRepository<Query, String> {

    Query findById(Id id);

    ArrayList<Query> findByQueryStatus(RequestStatus status);
}
