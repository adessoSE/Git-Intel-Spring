package de.adesso.gitstalker.core.REST.responses;

import de.adesso.gitstalker.core.enums.RequestType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ProcessingOrganization {

    @Id
    private String id;
    private String internalOrganizationName;

    private String processingMessage;
    private String searchedOrganization;
    private Set<RequestType> finishedRequestTypes;
    private Set<RequestType> missingRequestTypes;
    private int totalCountOfRequestTypes;
    private int totalCountOfNeededRequests;
    private int currentPositionInQueue;
}
