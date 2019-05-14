import { Member } from "./member";

export class Repository {
    public name: string;
    public description: string;
    public programmingLanguage: string;
    public license: string;
    public numOfContributors: number;
    public stars: number;
    public amountPreviousCommits: number;
    public amountPreviousPullRequests: number;
    public forks: number;
    public amountPreviousIssues: number;
    public contributors: Member[];
    public githubURL: string;
}