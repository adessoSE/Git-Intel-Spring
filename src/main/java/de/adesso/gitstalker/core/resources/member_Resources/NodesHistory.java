package de.adesso.gitstalker.core.resources.member_Resources;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class NodesHistory {

    private Calendar committedDate;
    private String url;
}
