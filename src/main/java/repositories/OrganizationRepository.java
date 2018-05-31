package repositories;

import objects.OrganizationWrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoRepository for storing organization and their crawled data.
 */
public interface OrganizationRepository extends MongoRepository<OrganizationWrapper, String> {
    OrganizationWrapper findByOrganizationName(String organizationName);
}

