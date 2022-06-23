import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {CscComponent} from "@calendar/calendar.component";
import {LogbookComponent} from "@app/logbook/logbook.component";

const routes: Routes = [
  {
    path: "",
    component: CscComponent
  }
  ,
  {
    path: "logbook",
    component: LogbookComponent

  },
  // {
  //     path: "registrations",
  //     component: RegistrationsViewComponent
  // },
  // {
  //     path: "integrations",
  //     component: IntegrationsViewComponent
  // }

]; // sets up routes constant where you define your routes


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class RoutingModule {
}
