package repositories;

import objects.OrganizationWrapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoRepository for storing organization and their crawled data.
 */
@Repository
public interface OrganizationRepository extends MongoRepository<OrganizationWrapper, String> {
    OrganizationWrapper findByOrganizationName(String organizationName);
}

