package de.adesso.gitstalker.core.resources.memberPR_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageInfo {

    private boolean hasNextPage;
    private String endCursor;
}
