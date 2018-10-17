package de.adesso.gitstalker.core.resources.memberPR_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodesMember {

    private PullRequests pullRequests;
    private String id;
}
