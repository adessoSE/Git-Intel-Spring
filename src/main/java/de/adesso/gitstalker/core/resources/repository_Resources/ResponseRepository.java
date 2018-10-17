package de.adesso.gitstalker.core.resources.repository_Resources;


import de.adesso.gitstalker.core.objects.Response;

public class ResponseRepository implements Response {

    private Data data;

    public ResponseRepository() {
    }

    public Data getData() {
        return data;
    }
}
