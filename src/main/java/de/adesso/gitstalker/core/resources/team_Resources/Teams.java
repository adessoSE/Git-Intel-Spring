package de.adesso.gitstalker.core.resources.team_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Teams {

    private PageInfo pageInfo;
    private ArrayList<NodesTeams> nodes;
    private int totalCount;
}
