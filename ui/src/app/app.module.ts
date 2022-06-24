import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
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
import {AngularDateHttpInterceptor} from "@shared/DateHttpInterceptor";
import {LogbookComponent} from './logbook/logbook.component';

@NgModule({
  declarations: [
    AppComponent,
    LogbookComponent
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
      serverLogLevel: NgxLoggerLevel.DEBUG
    }),
  ],
  providers: [CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AngularDateHttpInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule {


  constructor() {

  }
}
