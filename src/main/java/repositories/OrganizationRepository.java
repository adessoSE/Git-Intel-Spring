package repositories;

import entities.Level1.Level2.Organization;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoRepository for storing organizations and their associated data.
 */
public interface OrganizationRepository extends MongoRepository<Organization, String> {

    public Organization findByName(String name);

    public Organization findById(Id id);
}
