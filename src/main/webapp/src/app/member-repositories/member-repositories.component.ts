import { Component, OnInit, Input } from '@angular/core';
import { Repository } from '../entities/repository';
import { ActivatedRoute } from '@angular/router';
import { DataPullService } from '../services/data-pull.service';
import { GlobalNavigationService } from '../services/global-navigation.service';
import { Organization } from '../entities/organization';

@Component({
  selector: 'app-member-repositories',
  templateUrl: './member-repositories.component.html',
  styleUrls: ['./member-repositories.component.css']
})
export class MemberRepositoriesComponent implements OnInit {

  repositories: Repository[];
  repositoriesCopy: Repository[];

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
  
      this.dataPullService.requestMemberRepositories(org).subscribe(data => this.processData(data));
    }
  
    processData(repo: [Repository[]]) {
      this.repositories = this.processTotalRepositoriesOfMembers(repo);
      this.repositoriesCopy = this.repositories;
      this.navService.tellNumOfEntities(this.repositories.length);
      console.log(this.repositories);
    }

  processTotalRepositoriesOfMembers(repositories: [Repository[]]) {
    let totalRepositoriesOfMember: Repository[] = [];
    for (let repo of repositories) {
      totalRepositoriesOfMember = totalRepositoriesOfMember.concat(repo);
    }
    return totalRepositoriesOfMember;
  }

  sortByAlphabet() {
    this.repositories.sort((a: Repository, b: Repository) => a.name.localeCompare(b.name));
    this.sortByTag = "Alphabet";
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

  sortByStars() {
    this.repositories.sort((a: Repository, b: Repository) => {
      return +b.stars - +a.stars;
    });
    this.sortByTag = "Stars";
  }

  search(term: string) {
    setTimeout(() => {
      this.repositories = this.repositoriesCopy.filter(e => {
        for (let user of e.contributors) {
          return (e.name.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase()) || user.username.toLocaleLowerCase().includes(term.trim().toLocaleLowerCase()));
        }
      });
    }, 50);
  }
}
