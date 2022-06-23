import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'dateTransform'})
export class DateTransform implements PipeTransform {
  transform(date: string): string {

    return new Date(date).toLocaleString("de-DE", {
      year: 'numeric', month: "long", day: "numeric", hour: 'numeric', minute: 'numeric'
    })
  }
}
