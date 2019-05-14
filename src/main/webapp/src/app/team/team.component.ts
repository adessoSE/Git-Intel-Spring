import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Member } from '../entities/member';
import { Repository } from '../entities/repository';
import { Team } from '../entities/team';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';
import { CacheService } from '../services/cache.service';



@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {

  sortByTag: string = "";

  team: Team;

  teamMembers: Member[];
  teamMembersCopy: Member[];

  teamRepositories: Repository[];
  teamRepositoriesCopy: Repository[];

  statusCode: number;
  error: HttpErrorResponse;
  initializedProcessingInterval: boolean = false;
  interval: any;
  processingInformation: ProcessingOrganizationInfo;
  progressBarPercentage: number = 0;
  myStyles = {
    width: this.progressBarPercentage + "%"
  };

  constructor(
    private navService: GlobalNavigationService,
    private activatedRoute: ActivatedRoute,
    private dataPullService: DataPullService,
    private cacheService: CacheService) {

    // Set the number of "entities" displayed in the breadcrumbs to 0 so they are disabled in navigation-bar.component.html.
    this.navService.tellNumOfEntities(0);
    this.determineTeam();
  }

  ngOnInit() { }

  determineTeam() {
    let team = this.activatedRoute.snapshot.paramMap.get('name');
    let organization = this.activatedRoute.snapshot.paramMap.get('organization');
    this.cacheService.get(organization + 'Teams', this.dataPullService.requestTeams(organization)).subscribe(data => this.processData(data,team), error => this.processError(error));
  }

  initRequestInterval() {
    if (!this.initializedProcessingInterval) {
      this.initializedProcessingInterval = true;
      this.interval = setInterval( () => {
        this.determineTeam();
      }, 10000);
  }
}

initProgressBar() {   
  var progressBarIncreasementPerFinishedRequestType: number = 100 / this.processingInformation.totalCountOfRequestTypes;
  this.progressBarPercentage = (Math.round(progressBarIncreasementPerFinishedRequestType * 10) / 10) * this.processingInformation.finishedRequestTypes.length;
  this.myStyles.width = this.progressBarPercentage + "%";
}

processError(error: HttpErrorResponse) {
  this.statusCode = 400;
  this.error = error;
  console.log("Error Processing");
}

  processData(teams: HttpResponse<Team[]>, teamname: string) {
    switch (teams.status) {
      case 200:
        this.statusCode = 200;
        for (let team of teams.body) {
          if (team.name === teamname) {
            this.team = team;
            this.teamMembers = team.teamMembers;
            this.teamRepositories = team.teamRepositories;
          }
        }
        clearInterval(this.interval);
        break;
      case 202:
        this.statusCode = 202;
        this.processingInformation = JSON.parse(JSON.stringify(teams.body));
        console.log("Accepted - 202");
        this.initRequestInterval();
        this.initProgressBar();
        break;
    }
  }

  sortByAlphabet() {
    this.teamMembers.sort((a: Member, b: Member) => a.name.localeCompare(b.name));
    this.teamRepositories.sort((a: Repository, b: Repository) => a.name.localeCompare(b.name));
    this.sortByTag = "Alphabet";
  }

  sortByCommits() {
    this.teamMembers.sort((a: Member, b: Member) => {
      return +b.amountPreviousCommits - +a.amountPreviousCommits;
    });

    this.teamRepositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousCommits - +a.amountPreviousCommits;
    });
    this.sortByTag = "Commits";
  }

  sortByIssues() {
    this.teamMembers.sort((a: Member, b: Member) => {
      return +b.amountPreviousIssues - +a.amountPreviousIssues;
    });

    this.teamRepositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousIssues - +a.amountPreviousIssues;
    });
    this.sortByTag = "Issues";
  }

  sortByPullRequests() {
    this.teamMembers.sort((a: Member, b: Member) => {
      return +b.amountPreviousPullRequests - +a.amountPreviousPullRequests;
    });

    this.teamRepositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousPullRequests - +a.amountPreviousPullRequests;
    });
    this.sortByTag = "Pull Requests";
  }

  search(term: string) {
    this.teamMembers = this.teamMembersCopy.filter(e => {
      return e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase());
    });

    this.teamRepositories = this.teamRepositoriesCopy.filter(e => {
      return e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase());
    });
  }

  sumOf(numbers: Array<number>) {
    let sum = numbers.reduce((acc, cur) => acc + cur, 0);
    return sum;
  }

}
