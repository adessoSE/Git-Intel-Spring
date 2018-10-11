package de.adesso.gitstalker.core.resources.createdReposByMembers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class UserRepositories {

    private PageInfoRepositories pageInfo;
    private ArrayList<NodesRepositories> nodes;

}
