import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Repository } from '../entities/repository';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';

@Component({
  selector: 'app-external-repositories',
  templateUrl: './external-repositories.component.html',
  styleUrls: ['./external-repositories.component.css']
})
export class ExternalRepositoriesComponent implements OnInit {

  extRepos: Repository[];
  extReposCopy: Repository[];
  orgName: string = "";
  sortByTag: string = "";

  constructor(
    private activeRoute: ActivatedRoute,
    private dataPullService: DataPullService,
    private navService: GlobalNavigationService) { }

  ngOnInit() {
    this.determineOrganization();
  }

  determineOrganization() {
    let org = this.activeRoute.snapshot.paramMap.get('organization');

    this.dataPullService.requestExternalRepositories(org).subscribe(data => this.processData(data));
  }

  processData(repo: Repository[]) {
    this.extRepos = repo;
    this.extReposCopy = repo;
    this.navService.tellNumOfEntities(repo.length);
    console.log(repo);
  }

  sortByAlphabet() {
    this.extRepos.sort((a: Repository, b: Repository) => a.name.localeCompare(b.name));
    this.sortByTag = "Alphabet";
  }

  sortByCommits() {
    this.extRepos.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousCommits - +a.amountPreviousCommits;
    });
    this.sortByTag = "Commits";
  }

  sortByIssues() {
    this.extRepos.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousIssues - +a.amountPreviousIssues;
    });
    this.sortByTag = "Issues";
  }

  sortByForks() {
    this.extRepos.sort((a: Repository, b: Repository) => {
      return +b.forks - +a.forks;
    });
    this.sortByTag = "Forks";
  }

  sortByLicense() {
    this.extRepos.sort((a: Repository, b: Repository) => a.license.localeCompare(b.license));
    this.sortByTag = "License";
  }

  sortByPullRequests() {
    this.extRepos.sort((a: Repository, b: Repository) => {
      return +b.amountPreviousPullRequests - +a.amountPreviousPullRequests;
    });
    this.sortByTag = "Pull Requests";
  }

  sortByStars() {
    this.extRepos.sort((a: Repository, b: Repository) => {
      return +b.stars - +a.stars;
    });
    this.sortByTag = "Stars";
  }

  search(term: string) {
    setTimeout(() => {
      this.extRepos = this.extReposCopy.filter(e => {

        for (let user of e.contributors) {
          return (e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase()) || user.username.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase()));
        }
      });
    }, 50);
  }

  sumOf(numbers: Array<number>) {
    let sum = numbers.reduce((acc, cur) => acc + cur, 0);
    return sum;
  }
}
