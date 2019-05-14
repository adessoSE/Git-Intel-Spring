import { Component, OnInit } from '@angular/core';
import { Team } from '../entities/team';
import { DataPullService } from '../services/data-pull.service';
import { ActivatedRoute } from '../../../node_modules/@angular/router';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';
import { CacheService } from '../services/cache.service';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.css']
})
export class TeamsComponent implements OnInit {

  teams: Team[];
  teamsCopy: Team[];

  sortByTag: string = "";

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
    private dataPullService: DataPullService,
    private activeRoute: ActivatedRoute,
    private navService: GlobalNavigationService,
    private cacheService: CacheService) {
  }

  ngOnInit() {
    this.determineOrganization();
  }

  determineOrganization() {
    let organization = this.activeRoute.snapshot.paramMap.get('organization');
    this.cacheService.get(organization + 'Teams', this.dataPullService.requestTeams(organization)).subscribe(data => this.processData(data), error => this.processError(error));
  }

  initRequestInterval() {
    if (!this.initializedProcessingInterval) {
      this.initializedProcessingInterval = true;
      this.interval = setInterval( () => {
        this.determineOrganization()
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

  processData(teams: HttpResponse<Team[]>) {
    switch (teams.status) {
      case 200:
        this.statusCode = 200;
        this.teams = teams.body;
        this.teamsCopy = teams.body;
        this.navService.tellNumOfEntities(teams.body.length);
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
    this.teams.sort((a: Team, b: Team) => a.name.localeCompare(b.name));
    this.sortByTag = "Alphabet";
  }

  sortByMembers() {
    this.teams.sort((a: Team, b: Team) => {
      return +b.teamMembers.length - +a.teamMembers.length;
    });
    this.sortByTag = "Members";
  }

  sortByRepositories() {
    this.teams.sort((a: Team, b: Team) => {
      return +b.teamRepositories.length - +a.teamRepositories.length;
    });
    this.sortByTag = "Repositories";
  }

  search(term: string) {
    setTimeout(() => {
      this.teams = this.teamsCopy.filter(e => {
        return e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase());
      });
    }, 50);

  }
}
