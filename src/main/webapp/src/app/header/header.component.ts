import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, UrlSegment } from '@angular/router';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import { Tab } from '../entities/tab';
import { GlobalNavigationService } from '../services/global-navigation.service';

/**
 * Header component containing title message, search bar and tabs.
 */
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {


  organization: string = "";
  isSearchInvalid: boolean = false;
  isHome: boolean = false;

  tabs = new Array<Tab>();
  activeTabIdx: number = 0;

  constructor(
    private globalNavService: GlobalNavigationService,
    private router: Router,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
		/*
		 * Subscribes to "open a new Tab" requests, e.g. from HomeComponents
		 * searchHistory.
		 */
    this.globalNavService.onOpenNewTabEmitter.subscribe((tab) => {
      if (tab !== null && tab.org !== "home")
        this.openNewTab(tab)
    });

    // Hide input error on init
    this.hideWarning(true);

		/**
		 * Subscription returns an Array of Objects that look like so:
		 *     Object { path: "1. URL segment", parameters: {} }
		 *     Object { path: "2. URL segment", parameters: {} }
		 *     ...
		 * 
		 * The final URL is concatenated and if conditions are met, 
		 * opened in a new tab or otherwise assigned to the active tab. 
		 */
    this.router.events
      .filter((event) => event instanceof NavigationEnd)
      .map(() => this.activatedRoute)
      .map((route) => {
        while (route.firstChild) route = route.firstChild;
        return route;
      })
      .filter((route) => route.outlet === 'primary')
      .mergeMap((route) => route.url)
      .subscribe((event) => {
        let targetURL = this.concatURL(event);
        if (targetURL !== "home") {
          if (this.tabs.length === 0) {
            this.globalNavService.onOpenNewTab(targetURL);
          } else {
            this.tabs[this.activeTabIdx].url = targetURL;
          }
          this.isHome = false;
        }
        else {
          this.isHome = true;
        }
      });
  }

	/**
	 * Triggered by the search-button, the method
	 * does some format checking and to make sure no illegal
   * characters (&%$"!") are typed in.
   * Checks if a tab for the organisation is already open and, if it is, activates it.
	 */
  onClickStalk(org: string) {
    if (this.checkSearchInput(org)) {
      // Check if a tab is already open
      for (let tab of this.tabs) {
        if (tab.org === org) {
          this.activeTabIdx = this.tabs.indexOf(tab);
          this.router.navigate([tab.url]);
          return;
        }
      }
      this.isSearchInvalid = false;
      this.globalNavService.onOpenNewTab(org);
      // Clear input field
      this.organization = null;
      this.hideWarning(true);
    } else {
      this.isSearchInvalid = true;
      this.hideWarning(false);
    }
  }

	/**
	 * Pushes Tab into the global @tabs Array, hence opens it, marks it as active and navigates to given route. 
	 */
  openNewTab(tab: Tab) {
    console.log("---OPEN NEW TAB FUNCTION CALL---");
    this.tabs.push(tab);
    this.setActiveTab(this.tabs.length - 1);
    this.router.navigate([tab.url]);
    // Fetches the TabNameObject containing the url and name of the currently active organisation (displayed in the dashboard).
    // Walks through the list of tabs and looks for a match between a tab's url and the organisation's url (saved in the TabNameObject).
    this.globalNavService._tabNameObject.subscribe(tabName => {
      console.log("---SUBSCRIPTION EVENT---");
      for (let tab of this.tabs) {
        console.log("---FOR LOOP Tab--- " + tab);
        if (tabName != null) {
          if (tab.url === tabName.url) {
            tab.name = tabName.value;
          }
        }
      }
    });
  }

	/** 
	 * Closes tab and decides which tab to set as new active.
   * Cases:
   * 1) Active tab is closed and it's not the last one in the list --> The tab to the right of the closed tab is now active.
   * 2) Active tab is closed and it's the last one in the list --> The 'new' last tab is now active.
   * 3/ Active tab is closed and it's the only open tab --> No tabs. Home view is shown
   * 4) Any tab other than the active one is closed --> Active tab stays active.
	 */
  closeTab(idx: number) {
    this.tabs.splice(idx, 1);
    if (this.activeTabIdx == idx) {
      // Case 1)
      if (this.tabs[idx]) {
        this.activeTabIdx = idx;
        this.router.navigate(["/" + this.tabs[idx].url]);
      } // Case 2)
      else if (this.tabs.length >= 1) {
        this.activeTabIdx = idx - 1;
        this.router.navigate(["/" + this.tabs[idx - 1].url]);
      } // Case 3)
      else {
        this.router.navigate(["home"]);
      }
    } // Case 4)
    else {
      // If the active tab is 'right to' the closing tab, adjust indices 
      if (this.activeTabIdx > idx) {
        this.activeTabIdx = this.activeTabIdx - 1;
      }
      // If it's to the left, do nothing to keep the active tab active
    }
  }

  hideWarning(status: boolean) {
    document.getElementById("error").hidden = status;
  }

	/** 
	 * Assigns @activeTabIdx to index of given Tab. 
	 */
  setActiveTab(idx: number) {
    this.activeTabIdx = idx;
  }

	/** 
	 *  Input validation for organisation names (see registration on www.github.com):
	 *  Regex:
	 * - only alphanumeric characters or hyphens.
	 * - no multiple consecutive hyphens.
	 * - cannot begin or end with a hyphen.
	 * - maximum of 39 characters.
	*/
  checkSearchInput(login: string): boolean {
    return /^[a-z\d](?:[a-z\d]|-(?=[a-z\d])){0,38}$/i.test(login);
  }

	/** 
	 * Concatenates the given urlSegment seperated by "/"
	 * and removes the last one.
	 */
  concatURL(urlSegment: UrlSegment[]): string {
    let joinedURL = "";
    urlSegment.forEach(str => { joinedURL += str.toString() + "/" });
    joinedURL = joinedURL.substring(0, joinedURL.lastIndexOf("/"))

    return joinedURL;
  }

}
