package de.adesso.gitstalker.core.resources.rateLimit_Resources;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class RateLimit {

    private int cost;
    private int remaining;
    private Date resetAt;
}
