import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DateTransform} from "./date-transform";


@NgModule({
  declarations: [DateTransform],
  exports: [DateTransform],
  imports: [
    CommonModule
  ]
})
export class SharedModule {
}
