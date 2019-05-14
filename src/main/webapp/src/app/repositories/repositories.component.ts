import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Repository } from '../entities/repository';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { CacheService } from '../services/cache.service';

@Component({
  selector: 'app-repositories',
  templateUrl: './repositories.component.html',
  styleUrls: ['./repositories.component.css']
})
export class RepositoriesComponent implements OnInit {

  repositories: Repository[];
  repositoriesCopy: Repository[];

  orgName: string = "";
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
    private activeRoute: ActivatedRoute,
    private dataPullService: DataPullService,
    private navService: GlobalNavigationService,
    private cacheService: CacheService) { }

  ngOnInit() {
    this.determineOrganization();
  }

  determineOrganization() {
    let organization = this.activeRoute.snapshot.paramMap.get('organization');
    this.cacheService.get(organization + 'Repositories', this.dataPullService.requestRepositories(organization)).subscribe(data => this.processData(data), error => this.processError(error));
  }

  initRequestInterval() {
    if (!this.initializedProcessingInterval) {
      this.initializedProcessingInterval = true;
      this.interval = setInterval( () => {
        this.determineOrganization();
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

  processData(repo: HttpResponse<Repository[]>) {
    switch (repo.status) {
      case 200:
        this.statusCode = 200;
        this.repositories = repo.body;
        this.repositoriesCopy = repo.body;
        this.navService.tellNumOfEntities(repo.body.length);
        clearInterval(this.interval);
        break;
      case 202:
        this.statusCode = 202;
        this.processingInformation = JSON.parse(JSON.stringify(repo.body));
        console.log(this.processingInformation)
        console.log("Accepted - 202");
        this.initRequestInterval();
        this.initProgressBar();
        break;
    }
  }

  sortByAlphabet() {
    this.repositories.sort((a: Repository, b: Repository) => a.name.localeCompare(b.name));
    this.sortByTag = "Alphabet";
  }

  sortByCommits() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousCommits - +a.amountPreviousCommits;
    });
    this.sortByTag = "Commits";
  }

  sortByIssues() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousIssues - +a.amountPreviousIssues;
    });
    this.sortByTag = "Issues";
  }

  sortByForks() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.forks - +a.forks;
    });
    this.sortByTag = "Forks";
  }

  sortByLicense() {
    this.repositories.sort((a: Repository, b: Repository) => a.license.localeCompare(b.license));
    this.sortByTag = "License";
  }

  sortByPullRequests() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousPullRequests - +a.amountPreviousPullRequests;
    });
    this.sortByTag = "Pull Requests";
  }

  sortByStars() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.stars - +a.stars;
    });
    this.sortByTag = "Stars";
  }

  search(term: string) {
    setTimeout(() => {
      this.repositories = this.repositoriesCopy.filter(e => {
        return e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase());
      });
    }, 50);
  }
}
