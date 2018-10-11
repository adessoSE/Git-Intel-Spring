package de.adesso.gitstalker.core.resources.member_Resources;


import de.adesso.gitstalker.core.resources.rateLimit_Resources.RateLimit;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class Data {

    private User node;
    private RateLimit rateLimit;
}
