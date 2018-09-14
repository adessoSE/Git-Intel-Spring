package de.adesso.gitstalker.core.resources.memberPR_Resources;


import java.util.Calendar;

public class NodesPR {

    private Repository repository;
    private Calendar updatedAt;

    public NodesPR() {}

    public Repository getRepository() {
        return repository;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }
}
