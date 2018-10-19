package de.adesso.gitstalker.core.resources.createdReposByMembers;

import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class Data {

    private NodeUser node;
    private RateLimit rateLimit;
}
