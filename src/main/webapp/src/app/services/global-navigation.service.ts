import { Injectable } from '@angular/core';
import { BehaviorSubject } from "rxjs/BehaviorSubject";
import { Observable } from "rxjs/Observable";
import { Tab } from '../entities/tab';
import { TabNameObject } from '../entities/tabNameObject';


@Injectable()
export class GlobalNavigationService {

  organization: string = "";

  // Is used to display how many members/teams/repos there are - displayed in navigation bar.
  private _numOfEntities: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  public numOfEntitiesEmitter: Observable<number> = this._numOfEntities.asObservable();

  private _showNavBar: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public showNavBarEmitter: Observable<boolean> = this._showNavBar.asObservable();

  // Emitter und Listener for new tabs that are opened.
  public _openNewTab: BehaviorSubject<Tab> = new BehaviorSubject<Tab>(null);
  public onOpenNewTabEmitter: Observable<Tab> = this._openNewTab.asObservable();

  // Contains 2 values: 1) Name for the organisation's tab 2) Url parameter to match organisation and tab.
  public _tabNameObject: BehaviorSubject<TabNameObject> = new BehaviorSubject<TabNameObject>(null);

  constructor() { }

  tellNumOfEntities(n: number) {
    this._numOfEntities.next(n);
  }

  getNumOfEntities(): BehaviorSubject<number> {
    return this._numOfEntities;
  }

  showNavBar(ifShow: boolean) {
    this._showNavBar.next(ifShow);
  }

  /**
   * 
   * @param url 
   * Formats the organisation's URL and pushes the latest tab to the tab-BehaviourSubject.
   */
  onOpenNewTab(url: string) {
    let idxDash = url.indexOf("/");
    let idxSubstr = idxDash === -1 ? url.length : idxDash;

    let org = url.substring(0, idxSubstr);

    let tab: Tab = { org: org, url: url, name: "processing..." };
    this._openNewTab.next(tab)
  }

}
