import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {FormsModule} from '@angular/forms';
import {FlatpickrModule} from 'angularx-flatpickr';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {CscComponent, DayColorPipe, FormatDayRegistrationPipe} from "@calendar/calendar.component";


@NgModule({
  declarations: [CscComponent, FormatDayRegistrationPipe, DayColorPipe],
  exports: [CscComponent],

  imports: [
    CommonModule,
    FormsModule,
    NgbModalModule,
    FlatpickrModule.forRoot(),
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
  ]
})
export class CscCalendarModule {
}
