package de.adesso.gitstalker.core.resources.repository_Resources;

import de.adesso.gitstalker.core.objects.Response;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class ResponseRepository implements Response {

    private Data data;
}
