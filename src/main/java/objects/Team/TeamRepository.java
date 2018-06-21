package objects.Team;

public class TeamRepository {

    private String name;
    private int numOfCommits;

    public TeamRepository(String name, int numOfCommits) {
        this.name = name;
        this.numOfCommits = numOfCommits;
    }

    public String getName() {
        return name;
    }

    public int getNumOfCommits() {
        return numOfCommits;
    }
}
