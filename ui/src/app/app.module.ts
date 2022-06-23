import {HttpClientModule} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {RouterModule} from "@angular/router";
import {RoutingModule} from "@app/routing/routing.module";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CscCalendarModule} from "@calendar/csc-calendar.module";
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";

import {AppComponent} from "./app.component";
import {CookieService} from "ngx-cookie-service";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    CscCalendarModule,
    BrowserModule,
    HttpClientModule,
    RouterModule,
    RoutingModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG,
      serverLogLevel: NgxLoggerLevel.ERROR
    }),
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule {


  constructor() {

  }
}
