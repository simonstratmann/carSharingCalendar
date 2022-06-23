import {ChangeDetectionStrategy, Component, TemplateRef, ViewChild,} from '@angular/core';
import {isSameDay, isSameMonth,} from 'date-fns';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarEvent, CalendarEventAction, CalendarView,} from 'angular-calendar';
import {CarSharingEvent} from "@shared/carSharingCalendar";
import {map, tap} from "rxjs/operators";
import {NGXLogger} from "ngx-logger";
import {registerLocaleData} from "@angular/common";
import localeDe from "@angular/common/locales/de";
import localeDeExtra from "@angular/common/locales/extra/de";

const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA',
  },
};


@Component({
  selector: 'mwl-demo-component',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styles: [
    `
      h3 {
        margin: 0 0 10px;
      }

      pre {
        background-color: #f5f5f5;
        padding: 15px;
      }
    `,
  ],
  templateUrl: 'template.html',
})

export class CscComponent {
  @ViewChild('eventModalContent', {static: false}) eventModalContent: TemplateRef<any>;
  @ViewChild('newRegistrationModalContent', {static: false}) newRegistrationModalContent: TemplateRef<any>;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  events$: Observable<CalendarEvent<{ event: CarSharingEvent }>[]>;

  viewDate: Date = new Date();

  eventInfoModalData: {
    action: string;
    event: CalendarEvent;
  };

  newRegistrationModalData: CarSharingEvent;

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({event}: { event: CalendarEvent }): void => {
        this.logger.info("Clicked " + event);
        console.log("Clicked " + event)
        this.handleEvent('Edited ', event);
      },

    },
  ];

  handleEvent(action: string, event: CalendarEvent): void {
    this.eventInfoModalData = {event, action};
    this.modal.open(this.eventModalContent, {size: 'lg'});
  }

  refresh = new Subject<void>();


  activeDayIsOpen: boolean = true;

  constructor(private modal: NgbModal, private http: HttpClient, private logger: NGXLogger) {
    registerLocaleData(localeDe, 'de-DE', localeDeExtra);
  }

  ngOnInit(): void {
    this.fetchEvents();
  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0);
      this.viewDate = date;
    }
  }

  fetchEvents(): void {

    this.events$ = this.http
      .get('http://127.0.0.1:9000/api/registrations')
      .pipe(
        map((results: CarSharingEvent[]) => {
          return results.map((event: CarSharingEvent) => {
            this.logger.info(event);
            return {
              title: event.title,
              start: event.start,
              actions: this.actions,
              end: event.end,
              color: colors.blue,
              allDay: false,
              meta: {
                event: event
              }
            };
          });
        })
      )
      .pipe(tap(results => {
        results.sort((event1, event2) => event1.start - event2.start)
      }));

  }


  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  addEvent() {
    this.newRegistrationModalData = {};
    this.newRegistrationModalData.start = new Date();
    this.newRegistrationModalData.end = new Date();
    this.modal.open(this.newRegistrationModalContent, {size: 'lg'}).result.then((result) => {
      this.logger.info("Result ", result);
      this.http.post('http://127.0.0.1:9000/api/registrations', result).subscribe(response => {
        this.logger.info(response);
        console.log(response);
        this.fetchEvents();
      })
    });
  }
}
