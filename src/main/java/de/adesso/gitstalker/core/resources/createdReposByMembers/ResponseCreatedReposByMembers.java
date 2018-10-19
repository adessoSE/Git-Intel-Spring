package de.adesso.gitstalker.core.resources.createdReposByMembers;

import de.adesso.gitstalker.core.objects.Response;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
public class ResponseCreatedReposByMembers implements Response {

    private Data data;

}
