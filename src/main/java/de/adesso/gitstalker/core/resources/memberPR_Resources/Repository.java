package de.adesso.gitstalker.core.resources.memberPR_Resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Repository {

    private String id;
    @JsonProperty("isFork")
    private boolean isFork;
}
