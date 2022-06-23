import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DateTransform} from "./date-transform";
import {ShortenSha} from "@shared/shorten-sha-pipe";


@NgModule({
  declarations: [DateTransform, ShortenSha],
  exports: [DateTransform, ShortenSha],
  imports: [
    CommonModule
  ]
})
export class SharedModule {
}
