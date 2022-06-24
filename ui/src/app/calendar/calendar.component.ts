import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Pipe, PipeTransform, TemplateRef, ViewChild,} from '@angular/core';
import {endOfDay, isSameDay, isSameMonth, startOfDay,} from 'date-fns';
import {HttpClient} from '@angular/common/http';
import {Subject} from 'rxjs';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {CalendarEvent, CalendarEventAction, CalendarMonthViewDay, CalendarView} from 'angular-calendar';
import {Registration} from "@shared/carSharingCalendar";
import {NGXLogger} from "ngx-logger";
import {DatePipe, registerLocaleData} from "@angular/common";
import localeDe from "@angular/common/locales/de";
import localeDeExtra from "@angular/common/locales/extra/de";
import {ToastService} from "@app/toast/toast-service";
import {CookieService} from "ngx-cookie-service";

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
  black: {
    primary: '#cccccc',
    secondary: '#ffffff',
  },
};

interface Style {

  backgroundColor: string;
  description: string;
}


function userToColor(user: String) {
  if (user === "Hilde") {
    return colors.blue;
  } else if (user === "Kuni") {
    return colors.yellow
  } else if (user === "Marianne") {
    return colors.red;
  } else {
    return colors.black;
  }
}


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


  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  events: CalendarEvent<{ event: Registration, style: Style }>[];
  loaded: boolean = false;

  viewDate: Date = new Date();

  eventInfoModalData: {
    action: string;
    event: CalendarEvent;
  };

  newRegistrationModalData: Registration;

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

  ngOnInit(): void {
    this.fetchEvents();
  }

  handleEvent(action: string, event: CalendarEvent): void {
    this.eventInfoModalData = {event, action};
    this.modal.open(this.eventModalContent, {size: 'lg'});
  }

  refresh = new Subject<void>();


  activeDayIsOpen: boolean = false;

  constructor(private modal: NgbModal, private http: HttpClient, private logger: NGXLogger, private changeDetection: ChangeDetectorRef, private toastService: ToastService, private cookieService: CookieService) {
    registerLocaleData(localeDe, 'de-DE', localeDeExtra);
  }


  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0);
      this.viewDate = date;
    }
  }

  fetchEvents(): void {
    this.logger.info("Loading registrations")
    this.http
      .get('http://127.0.0.1:9000/api/registrations')
      .subscribe((events: Registration[]) => {
        this.logger.info("Received ", events.length, " registration entries")
        this.events = [];
        events.forEach(event => {
          this.events.push({
            title: event.title,
            start: event.start,
            actions: this.actions,
            end: event.end,
            color: userToColor(event.username),
            meta: {
              style: {
                backgroundColor: "",
                description: ""
              },
              event: event,

            }
          });
        });
        this.loaded = true;
        this.logger.info(this.events.length);
        this.changeDetection.detectChanges();
      });

  }


  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  private ngbModalRef: NgbModalRef;


  addEvent() {


  }

  deleteRegistration(event: Registration) {
    this.http.delete('http://127.0.0.1:9000/api/registrations/' + event.id).subscribe(response => {
      this.logger.info(response);
      this.fetchEvents();
      this.toastService.show('Registrierung gelöscht', {classname: 'bg-success text-light'});
    }, response => {
      this.toastService.show(response, {classname: 'bg-danger text-light'});
    })
  }


}

@Pipe({name: 'formatDayRegistration'})
export class FormatDayRegistrationPipe implements PipeTransform {

  constructor(private datepipe: DatePipe, private logger: NGXLogger) {
  }

  transform(event: CalendarEvent, day: CalendarMonthViewDay) {
    let fullDesc = event.meta.event.username;
    let timeDesc = "";
    if (event.start < day.date && event.end > endOfDay(day.date)) {
      timeDesc = "";
    } else if (event.start < day.date && event.end < endOfDay(day.date)) {
      timeDesc = "Bis " + this.datepipe.transform(event.end, "H:mm");
    } else if (event.end > startOfDay(day.date) && event.end > endOfDay(day.date)) {
      timeDesc = "Ab " + this.datepipe.transform(event.start, "H:mm");
    } else {
      timeDesc = this.datepipe.transform(event.start, "H:mm") + "-" + this.datepipe.transform(event.end, "H:mm");
    }
    if (timeDesc) {
      fullDesc += ": " + timeDesc
    }
    if (event.title) {
      fullDesc += " (" + event.title + ")";
    }
    return fullDesc;
  }
}

@Pipe({name: 'dayColor'})
export class DayColorPipe implements PipeTransform {

  constructor() {
  }

  transform(day: CalendarMonthViewDay) {
    let bgColor;
    if (day.events.length !== 1) {
      bgColor = '#ffffff';
    } else if (day.events[0].start < day.date && day.events[0].end > endOfDay(day.date)) {
      bgColor = userToColor(day.events[0].meta.event.user).primary;
    } else {
      bgColor = '#ffffff';
    }
    return bgColor;
  }
}
