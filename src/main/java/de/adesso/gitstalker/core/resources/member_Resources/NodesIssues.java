package de.adesso.gitstalker.core.resources.member_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class NodesIssues {

    private Calendar createdAt;
    private String url;
}
