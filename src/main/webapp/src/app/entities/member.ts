import { Organization } from "./organization";

export class Member {
    public username: string;
    public name: string;
    public avatarURL: string;
    public githubURL: string;
    public organization: Organization;
    public amountPreviousCommits: number;
    public amountPreviousPullRequests: number;
    public amountPreviousIssues: number;
    public previousCommits: any;
    public previousPullRequests: any;
    public previousIssues: any;
    public previousCommitsWithLink: any;
    public previousPullRequestsWithLink: any;
    public previousIssuesWithLink: any;
}