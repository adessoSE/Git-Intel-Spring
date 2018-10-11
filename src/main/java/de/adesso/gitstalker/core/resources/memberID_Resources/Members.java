package de.adesso.gitstalker.core.resources.memberID_Resources;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Members {

    private ArrayList<Nodes> nodes;
    private PageInfo pageInfo;
}
