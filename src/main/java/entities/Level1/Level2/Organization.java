package entities.Level1.Level2;

import entities.Level1.Level2.Level3.Members;
import org.springframework.data.annotation.Id;

public class Organization {

    @Id
    private String id;

    private String name;
    private Members members;

    public Organization() {
    }

    public Members getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
