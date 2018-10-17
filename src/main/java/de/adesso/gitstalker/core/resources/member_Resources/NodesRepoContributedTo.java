package de.adesso.gitstalker.core.resources.member_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodesRepoContributedTo {

    private DefaultBranchRef defaultBranchRef;
    private String id;
}
