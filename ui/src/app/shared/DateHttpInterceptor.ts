import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

export class AngularDateHttpInterceptor implements HttpInterceptor {
  // Migrated from AngularJS https://raw.githubusercontent.com/Ins87/angular-date-interceptor/master/src/angular-date-interceptor.js
  iso8601 = /^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(\.\d+)?(([+-]\d\d:\d\d)|Z)?$/;

  public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap({
        next: (event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            const body = event.body;
            this.convertToDate(body);
          }
        },
        error: (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 401) {
            }
          }
        },
      }),
    );
  }

  convertToDate(body) {
    if (typeof body === 'object' && body !== null) {
      const casted = body as { [key: string]: unknown };
      for (const key of Object.keys(casted)) {
        const value = casted[key];
        if (this.isIso8601(value)) {
          casted[key] = new Date(value);
        } else if (typeof value === 'object' && value !== null) {
          this.convertToDate(value);
        }
      }
    } else {
      return body;
    }
  }

  isIso8601(value: unknown): value is string {
    return typeof value === 'string' ? this.iso8601.test(value) : false;
  }
}
