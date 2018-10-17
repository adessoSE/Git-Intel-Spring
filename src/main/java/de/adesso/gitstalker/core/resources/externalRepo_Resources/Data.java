package de.adesso.gitstalker.core.resources.externalRepo_Resources;

import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@lombok.Data
@NoArgsConstructor
public class Data {

    private ArrayList<NodesRepositories> nodes;
    private RateLimit rateLimit;
}
