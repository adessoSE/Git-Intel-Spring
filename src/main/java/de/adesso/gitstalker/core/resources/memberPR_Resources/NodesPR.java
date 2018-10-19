package de.adesso.gitstalker.core.resources.memberPR_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class NodesPR {

    private Repository repository;
    private Calendar updatedAt;
}
