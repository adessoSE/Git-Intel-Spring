import { Member } from "./member";
import { Repository } from "./repository";

export class Team {
    public name: string;
    public description: string;
    public teamRepositories: Repository[];
    public teamMembers: Member[];
    public numOfMembers: number;
    public commits: number;
    public avatarURL: string;
    public githubURL: string;
}