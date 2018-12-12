package de.adesso.gitstalker.core.repositories;

import de.adesso.gitstalker.core.REST.responses.ProcessingOrganization;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoRepository for the currently processing organizations.
 */
public interface ProcessingRepository extends MongoRepository<ProcessingOrganization, Integer> {
    ProcessingOrganization findByInternalOrganizationName(String organizationName);

    void deleteByInternalOrganizationName(String organizationName);
}

