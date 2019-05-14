import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
// Separate internal module for routing containing paths, etc.
import { AppRoutingModule } from './/app-routing.module';
import { FormsModule } from '@angular/forms';
import { PopoverModule } from 'ngx-popover';
import { ChartsModule } from 'ng2-charts';

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MembersComponent } from './members/members.component';
import { TeamsComponent } from './teams/teams.component';
import { RepositoriesComponent } from './repositories/repositories.component';
import { ExternalRepositoriesComponent } from './external-repositories/external-repositories.component';
import { MemberRepositoriesComponent } from './member-repositories/member-repositories.component';
import { MemberComponent } from './member/member.component';
import { HomeComponent } from './home/home.component';
import { TeamComponent } from './team/team.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { GraphComponent } from './graph/graph.component';

import { GlobalNavigationService } from './services/global-navigation.service';
import { DataPullService } from './services/data-pull.service';

import { CapitalizePipe } from './pipes/capitalize.pipe';
import { LastWordPipe } from './pipes/last-word.pipe';
import { PrepTabNamePipe } from './pipes/prep-tab-name.pipe';
import { HttpClientModule } from '@angular/common/http';
import { ProcessingComponent } from './processing/processing.component';
import { ErrorComponent } from './error/error.component';
import { CacheService } from './services/cache.service';
import { HeaderComponent } from './header/header.component';
import { TileComponent } from './dashboard/tile/tile.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    MembersComponent,
    TeamsComponent,
    RepositoriesComponent,
    ExternalRepositoriesComponent,
    MemberRepositoriesComponent,
    MemberComponent,
    HomeComponent,
    NavigationBarComponent,
    TeamComponent,
    GraphComponent,
    CapitalizePipe,
    LastWordPipe,
    PrepTabNamePipe,
    ProcessingComponent,
    ErrorComponent,
    HeaderComponent,
    TileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    PopoverModule,
    ChartsModule,
    HttpClientModule
  ],
  providers: [
    GlobalNavigationService,
    DataPullService,
    CacheService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
