import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ChartJsData } from '../entities/chartJS';
import { Organization } from '../entities/organization';
import { ProcessingOrganizationInfo } from '../entities/processingOrganizationInfo';
import { CacheService } from '../services/cache.service';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { TabNameObject } from '../entities/tabNameObject';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {

  organization: Organization;
  chartMembers: ChartJsData;
  chartCommits: ChartJsData;
  chartPRs: ChartJsData;

  tabName: TabNameObject;

  statusCode: number;
  error: HttpErrorResponse;
  processingInformation: ProcessingOrganizationInfo;
  progressBarPercentage: number = 0;
  initializedProcessingInterval: boolean = false;
  interval: any;

  navigationSubscription;

  constructor(
    private activeRoute: ActivatedRoute,
    private router: Router,
    private globalNavService: GlobalNavigationService,
    private dataPullService: DataPullService,
    private cacheService: CacheService) {

    /**
     * Listens to routing events and renders the dashboard according to the active route.
     */
    this.navigationSubscription = router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.determineOrganization();
        this.initializedProcessingInterval = false;
        clearInterval(this.interval);
      }
    });
  }

  /** 
   * Disables NavigationBar while on the Dashboard. 
   */
  ngOnInit() {
    this.globalNavService.showNavBar(false);
  }

  /** 
   * Displays NavigationBar as user leaves the Dashboard.
   */
  ngOnDestroy() {
    // Unsubscribe from router events to prevent further triggering of events
    this.navigationSubscription.unsubscribe();
    this.globalNavService.showNavBar(true);
    clearInterval(this.interval);
  }

  /** 
   * Reads the URL parameter and calls @globalNavService to fetch organization data from Backend.
   */
  determineOrganization() {
    let organization = this.activeRoute.snapshot.paramMap.get('organization');
    this.cacheService.get(organization + 'Organization', this.dataPullService.requestOrganization(organization)).subscribe(data => this.processData(data), error => this.processError(error));
  }

  /**
   * Determines the interval in which the progress of a currently processed organisation is checked.
   */
  initRequestInterval() {
    if (!this.initializedProcessingInterval) {
      this.initializedProcessingInterval = true;
      this.interval = setInterval(() => {
        this.determineOrganization();
      }, 10000);
    }
  }

  /**
   * Determines how the progress of the progress bar is calculated.
   */
  initProgressBar() {
    var progressBarIncreasementPerFinishedRequestType: number = 100 / this.processingInformation.totalCountOfRequestTypes;
    this.progressBarPercentage = (Math.round(progressBarIncreasementPerFinishedRequestType * 10) / 10) * this.processingInformation.finishedRequestTypes.length;
  }

  processError(error: HttpErrorResponse) {
    this.statusCode = 400;
    this.error = error;
    console.log("Error Processing");
  }

  /**
   * 
   * @param orga 
   * Differentiates between two cases:
   * 1) Data is available -> Displays data in dashboard.
   * 2) Data needs to be gathered -> Displays progress indicators in dashboard.
   */
  processData(orga: HttpResponse<Organization>) {
    console.log("Processing Organization!");
    switch (orga.status) {
      case 200:
        this.statusCode = 200;
        this.organization = orga.body;
        this.generateTabName(this.organization);
        // Sends the generated tabNameObject to the GlobalNavService for the header component to access.
        this.globalNavService._tabNameObject.next(this.tabName);
        console.log(this.organization);
        clearInterval(this.interval);
        this.initGraphs();
        break;
      case 202:
        this.statusCode = 202;
        this.processingInformation = JSON.parse(JSON.stringify(orga.body));
        console.log(this.processingInformation)
        console.log("Accepted - 202");
        this.initRequestInterval();
        this.initProgressBar();
        break;
    }
  }

  generateTabName(orga: Organization) {
    let url = this.activeRoute.snapshot.paramMap.get('organization');
    this.tabName = { url: url, value: orga.name };
  }

  /**
   * Initiates the data graphs using the predefined object structure.
   */
  initGraphs() {
    this.chartCommits = {
      labels: this.organization.internalRepositoriesCommits.chartJSLabels,
      data: [{ data: this.organization.internalRepositoriesCommits.chartJSDataset, label: "Commits" }],
      caption: "Commits to internal repositories"
    };
    this.chartPRs = {
      labels: this.organization.externalRepositoriesPullRequests.chartJSLabels,
      data: [{ data: this.organization.externalRepositoriesPullRequests.chartJSDataset, label: "Pull Requests" }],
      caption: "Pull Requests to external repositories"
    };
    this.chartMembers = {
      labels: this.organization.memberAmountHistory.chartJSLabels,
      data: [{ data: this.organization.memberAmountHistory.chartJSDataset, label: "Members" }],
      caption: "Members"
    };
  }
}
