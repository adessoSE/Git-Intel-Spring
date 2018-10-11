package de.adesso.gitstalker.core.resources.memberPR_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class PullRequests {

    private ArrayList<NodesPR> nodes;
}
