import { Component, OnInit, Output } from '@angular/core';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { CacheService } from '../services/cache.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  searchHistory: Set<string> = new Set<string>();

  constructor(private globalNavService: GlobalNavigationService,
    private cacheService: CacheService) { }

  /** 
   * Disables NavigationBar while on HomeComponent
   * and saves entries of searched organization in a Set
   */
  ngOnInit() {
    this.globalNavService.showNavBar(false);

    this.globalNavService.onOpenNewTabEmitter.subscribe((tab) => {
      if (tab !== null){
        if(this.cacheService.has("searchHistory")){
          let mergedSearchedHistory = new Set<string>();
          this.cacheService.get("searchHistory").subscribe(data => {
            this.searchHistory = data;
            this.searchHistory.add(tab.org);
          });
        } else {
          this.searchHistory.add(tab.org);
        }
      this.cacheService.set("searchHistory", this.searchHistory);
      }    
    })
  }

  /** 
   * Displays NavigationBar as user leaves the HomeComponent.
   */
  ngOnDestroy() {
    this.globalNavService.showNavBar(true);
  }

  /** 
   * Requests @globalNavService to open a new Tab for given organization.
   */
  openNewTab(org: string) {
    this.globalNavService.onOpenNewTab(org);
  }

}
