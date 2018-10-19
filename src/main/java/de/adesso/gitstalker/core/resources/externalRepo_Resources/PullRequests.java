package de.adesso.gitstalker.core.resources.externalRepo_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class PullRequests {

    private ArrayList<NodesPullRequests> nodes;

}
