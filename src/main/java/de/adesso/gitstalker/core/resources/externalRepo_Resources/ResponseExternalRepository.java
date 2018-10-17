package de.adesso.gitstalker.core.resources.externalRepo_Resources;

import de.adesso.gitstalker.core.objects.Response;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class ResponseExternalRepository implements Response {

    private Data data;
}
