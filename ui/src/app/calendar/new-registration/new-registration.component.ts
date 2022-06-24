import {ChangeDetectorRef, Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Registration} from "@shared/carSharingCalendar";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {CookieService} from "ngx-cookie-service";
import {NGXLogger} from "ngx-logger";
import {ToastService} from "@toast/toast-service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-new-registration',
  templateUrl: './new-registration.component.html',
  styleUrls: ['./new-registration.component.scss']
})
export class NewRegistrationComponent implements OnInit {

  @Input() onNew: Function;

  @ViewChild('newRegistrationModalContent', {static: false}) modalContent: TemplateRef<any>;
  registration: Registration;
  private ngbModalRef: NgbModalRef;

  constructor(private modalService: NgbModal, private http: HttpClient, private logger: NGXLogger, private changeDetection: ChangeDetectorRef, private toastService: ToastService, private cookieService: CookieService) {
  }

  ngOnInit(): void {
  }

  open(): void {
    this.registration = {};
    this.registration.username = this.cookieService.get("username");
    this.registration.start = new Date();
    this.registration.start.setMinutes(0);
    this.registration.start.setSeconds(0);
    this.registration.start.setHours(this.registration.start.getHours() + 1);
    this.registration.end = new Date();
    this.registration.end.setMinutes(0);
    this.registration.end.setSeconds(0);
    this.registration.end.setHours(this.registration.end.getHours() + 2);
    this.ngbModalRef = this.modalService.open(this.modalContent, {size: 'lg'});

    this.ngbModalRef.result.then((result: Registration) => {
      if (!result) {
        return;
      }
      this.logger.info("Adding new registration: ", result);
      this.cookieService.set("username", result.username);
      this.http.post('http://127.0.0.1:9000/api/registrations', result).subscribe(response => {
        this.logger.info(response);
        this.toastService.show('Registrierung hinzugefügt', {classname: 'bg-success text-light'});
        this.onNew();
        this.changeDetection.detectChanges();

      }, response => {
        this.toastService.show(response, {classname: 'bg-danger text-light'});
      })
    }, rejection => {

    });
  }


}
