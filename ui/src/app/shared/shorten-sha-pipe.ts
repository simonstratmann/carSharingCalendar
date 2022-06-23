import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'shortenSha'})
export class ShortenSha implements PipeTransform {
  transform(text: string): string {
    //f531640b481f425e49faf62bed60b6b79958228a

    let output = !text ? text : text.replace(/'([a-f0-9]{7})[a-f0-9]{33}'/, "'$1'");
    console.log("Input: " + text);
    console.log("Output: " + output);
    return output;
  }
}
