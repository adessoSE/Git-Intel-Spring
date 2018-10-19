package de.adesso.gitstalker.core.resources.createdReposByMembers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageInfoRepositories {

    @JsonProperty("hasNextPage")
    private boolean hasNextPage;
    private String endCursor;

}
