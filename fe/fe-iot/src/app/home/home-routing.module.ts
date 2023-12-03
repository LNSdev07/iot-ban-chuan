import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ReportComponent } from './report/report.component';
import { DataComponent } from './data/data.component';
import { ActionComponent } from './action/action.component';
import { UserComponent } from './user/user.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      { path: 'report', component: ReportComponent },
      { path: 'data', component: DataComponent },
      { path: 'action', component: ActionComponent },
      { path: 'user', component: UserComponent }
      // ... other routes for HomeComponent
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
