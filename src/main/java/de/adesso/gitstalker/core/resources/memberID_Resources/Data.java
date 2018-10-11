package de.adesso.gitstalker.core.resources.memberID_Resources;

import de.adesso.gitstalker.core.resources.memberPR_Resources.NodesMember;
import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@lombok.Data
@NoArgsConstructor
public class Data {

    private Organization organization;
    private ArrayList<NodesMember> nodes;
    private RateLimit rateLimit;
}
