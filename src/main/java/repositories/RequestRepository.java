package repositories;

import enums.RequestStatus;
import objects.Query;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * MongoRepository for storing organizations and their associated data.
 */
public interface RequestRepository extends MongoRepository<Query, String> {

    Query findById(Id id);
    ArrayList<Query> findByRequestStatus(RequestStatus status);
}
