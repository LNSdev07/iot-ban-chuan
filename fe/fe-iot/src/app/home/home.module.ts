import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home/home.component';
import { SharedModule } from '../shared/shared.module';
import { IconsProviderModule } from '../icons-provider.module';
import { DataComponent } from './data/data.component';
import { ReportComponent } from './report/report.component';
import { ActionComponent } from './action/action.component';
import { UserComponent } from './user/user.component';

@NgModule({
  declarations: [
    HomeComponent,
    DataComponent,
    ReportComponent,
    ActionComponent,
    UserComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    SharedModule,
    IconsProviderModule,
  ]
})
export class HomeModule { }
