import {ChangeDetectionStrategy, Component, TemplateRef, ViewChild,} from '@angular/core';
import {isSameDay, isSameMonth,} from 'date-fns';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CalendarEvent, CalendarEventAction, CalendarView,} from 'angular-calendar';
import {CarSharingEvent} from "@shared/carSharingCalendar";
import {map} from "rxjs/operators";
import {NGXLogger} from "ngx-logger";

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
  @ViewChild('modalContent', {static: true}) modalContent: TemplateRef<any>;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  events$: Observable<CalendarEvent<{ film: CarSharingEvent }>[]>;

  viewDate: Date = new Date();

  modalData: {
    action: string;
    event: CalendarEvent;
  };

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({event}: { event: CalendarEvent }): void => {
        // this.handleEvent('Edited ', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({event}: { event: CalendarEvent }): void => {
        // this.events = this.events.filter((iEvent) => iEvent !== event);
        // this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh = new Subject<void>();


  activeDayIsOpen: boolean = true;

  constructor(private modal: NgbModal, private http: HttpClient, private logger: NGXLogger) {
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


    // this.events$ = this.http
    //   .get('http://127.0.0.1:9000/api/events')
    //   .pipe(
    //     map(({ results }: { results: CarSharingEvent[] }) => {
    //       return results.map((event: CarSharingEvent) => {
    //         return {
    //           title: event.title,
    //           start: event.start,
    //
    //           end: event.end,
    //           color: null,
    //           allDay: false
    //         };
    //       });
    //     })
    //   );
    this.events$ = this.http
      .get('http://127.0.0.1:9000/api/events')

      .pipe(
        map((results: CarSharingEvent[]) => {
          return results.map((event: CarSharingEvent) => {
            this.logger.info(event);
            return {
              title: event.title,
              start: event.start,

              end: event.end,
              color: colors.blue,
              allDay: false
            };
          });
        })
      );
  }

  // eventTimesChanged({
  //                     event,
  //                     newStart,
  //                     newEnd,
  //                   }: CalendarEventTimesChangedEvent): void {
  //   this.events = this.events.map((iEvent) => {
  //     if (iEvent === event) {
  //       return {
  //         ...event,
  //         start: newStart,
  //         end: newEnd,
  //       };
  //     }
  //     return iEvent;
  //   });
  //   this.handleEvent('Dropped or resized ', event);
  // }
  //
  // handleEvent(action: string, event: CalendarEvent): void {
  //   this.modalData = {event, action};
  //   this.modal.open(this.modalContent, {size: 'lg'});
  // }
  //
  // addEvent(): void {
  //   this.events = [
  //     ...this.events,
  //     {
  //       title: 'New event',
  //       start: startOfDay(new Date()),
  //       end: endOfDay(new Date()),
  //       color: colors.red,
  //       draggable: true,
  //       resizable: {
  //         beforeStart: true,
  //         afterEnd: true,
  //       },
  //     },
  //   ];
  // }
  //
  // deleteEvent(eventToDelete: CalendarEvent) {
  //   this.events = this.events.filter((event) => event !== eventToDelete);
  // }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }
}
