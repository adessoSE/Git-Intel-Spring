import { Component, OnInit } from '@angular/core';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { Location } from '@angular/common';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';

@Component({
  selector: 'navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  showNavigation: boolean = true;
  routeHistory: string[] = [];
  numOfEntities: number = 0;

  constructor(
    private globalNavService: GlobalNavigationService,
    private location: Location,
    private router: Router,
    private activeRoute: ActivatedRoute) { }

  ngOnInit() {
    // Subscribe to navigation and routing services
    this.globalNavService.showNavBarEmitter.subscribe((mode) => { this.showNavigation = mode });
    this.globalNavService.numOfEntitiesEmitter.subscribe((n) => { this.numOfEntities = n });
    this.router.events.subscribe((val) => { this.prepareRouteHistory(this.location.path()) });

    /* 
     * Necessary for enabling Navigation Bar if navigating via URL
     */ 
    this.globalNavService.showNavBar(true);
  }


  prepareRouteHistory(url: string) {

    let h: string[] = [];

    h = url.split("/");
    h.shift();

    for (let i = 1; i < h.length; i++) {
      h[i] = h[i - 1] + "/" + h[i];
    }

    this.routeHistory = h;
  }

  goBack() {
    this.location.back();
  }

}