import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChartJsData } from '../entities/chartJS';
import { Member } from '../entities/member';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { TableDataObject } from '../entities/tableDataObject';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { CacheService } from '../services/cache.service';

@Component({
  selector: 'app-member',
  templateUrl: './member.component.html',
  styleUrls: ['./member.component.css']
})
export class MemberComponent implements OnInit {

  member: Member;
  chartCommits: ChartJsData;
  chartPRs: ChartJsData;
  chartIssues: ChartJsData;

  commitsTableData: TableDataObject[] = [];
  pullRequestsTableData: TableDataObject[] = [];
  issuesTableData: TableDataObject[] = [];

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

    // Set the number of "entities" displayed in the breadcrumbs to 0 so they are disabled in navigation-bar.component.html.
    this.navService.tellNumOfEntities(0);
    this.determineMember();
  }

  ngOnInit() {
    // this.initGraphs();
  }
  
  initRequestInterval(username: String) {
    if (!this.initializedProcessingInterval) {
      this.initializedProcessingInterval = true;
      this.interval = setInterval( () => {
        this.determineMember();
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

  processData(members: HttpResponse<Member[]>, username: String) {
    switch (members.status) {
      case 200:
        this.statusCode = 200;
        for (let member of members.body) {
          if (member.username === username) {
            this.member = member;
          }
        }
        this.initGraphs();
        this.processTableData();
        clearInterval(this.interval);
        break;
      case 202:
        this.statusCode = 202;
        this.processingInformation = JSON.parse(JSON.stringify(members.body));
        console.log(this.processingInformation)
        console.log("Accepted - 202");
        this.initRequestInterval(username);
        this.initProgressBar();
        break;
    }
  }

  determineMember() {
    let member = this.activeRoute.snapshot.paramMap.get('username');
    let organization = this.activeRoute.snapshot.paramMap.get('organization');
    this.cacheService.get(organization + 'Members', this.dataPullService.requestMembers(organization)).subscribe(data => this.processData(data, member), error => this.processError(error));
  }

  initGraphs() {
    this.chartCommits = {
      labels: this.member.previousCommits.chartJSLabels,
      data: [{ data: this.member.previousCommits.chartJSDataset, label: "Commits" }],
      caption: "Latest commits"
    };
    this.chartPRs = {
      labels: this.member.previousPullRequests.chartJSLabels,
      data: [{ data: this.member.previousPullRequests.chartJSDataset, label: "Pull Requests" }],
      caption: "Latest Pull Requests"
    };
    this.chartIssues = {
      labels: this.member.previousIssues.chartJSLabels,
      data: [{ data: this.member.previousIssues.chartJSDataset, label: "Issues" }],
      caption: "Latest Issues"
    };
  }

  /**
   * Processes commit/issue/pr data for Table by creating appropriate TableDataObjects and pushing them into an array for iteration.
   */
  processTableData() {
    let tableDataObject: TableDataObject;
    for (var key in this.member.previousCommitsWithLink) {
      if (this.member.previousCommitsWithLink.hasOwnProperty(key)) {
        let formattedDate = this.formatDate(key);
        tableDataObject = new TableDataObject(formattedDate, this.member.previousCommitsWithLink[key]);
        this.commitsTableData.push(tableDataObject);
      }
    }
    for (var key in this.member.previousPullRequestsWithLink) {
      if (this.member.previousPullRequestsWithLink.hasOwnProperty(key)) {
        let formattedDate = this.formatDate(key);
        tableDataObject = new TableDataObject(formattedDate, this.member.previousPullRequestsWithLink[key]);
        this.pullRequestsTableData.push(tableDataObject);
      }
    }
    for (var key in this.member.previousIssuesWithLink) {
      if (this.member.previousIssuesWithLink.hasOwnProperty(key)) {
        let formattedDate = this.formatDate(key);
        tableDataObject = new TableDataObject(formattedDate, this.member.previousIssuesWithLink[key]);
        this.issuesTableData.push(tableDataObject);
      }
    }
    this.sortTableData(this.commitsTableData);
    this.sortTableData(this.issuesTableData);
    this.sortTableData(this.pullRequestsTableData);
  }

  formatDate(stringDate: string): Date {
    let year = stringDate.split(" ");
    let formattedDate = year[0] + " " + year[1] + " " + year[2] + " " + year[5] + " " + year[3];
    return new Date(formattedDate);
  }

  sortTableData(objects: TableDataObject[]) {
    objects.sort((a: TableDataObject, b: TableDataObject) => {
      return a.date.getTime() - b.date.getTime();
    });
  }
}
