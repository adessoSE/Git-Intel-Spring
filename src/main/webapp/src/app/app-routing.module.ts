import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { MembersComponent } from './members/members.component';
import { TeamsComponent } from './teams/teams.component';
import { RepositoriesComponent } from './repositories/repositories.component';
import { ExternalRepositoriesComponent } from './external-repositories/external-repositories.component';
import { MemberComponent } from './member/member.component';
import { HomeComponent } from './home/home.component';
import { TeamComponent } from './team/team.component';
import { MemberRepositoriesComponent } from './member-repositories/member-repositories.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: ':organization', component: DashboardComponent, runGuardsAndResolvers: 'paramsChange' },
  { path: ':organization/members', component: MembersComponent },
  { path: ':organization/members/:username', component: MemberComponent },
  { path: ':organization/teams', component: TeamsComponent },
  { path: ':organization/teams/:name', component: TeamComponent },
  { path: ':organization/teams/:name/:username', component: MemberComponent },
  { path: ':organization/repositories', component: RepositoriesComponent },
  { path: ':organization/external-repositories', component: ExternalRepositoriesComponent },
  { path: ':organization/member-repositories', component: MemberRepositoriesComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
