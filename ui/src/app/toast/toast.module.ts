import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NgbdToastGlobal} from './toast-global.component';
import {ToastsContainer} from './toast-container.component';

@NgModule({
  declarations: [NgbdToastGlobal, ToastsContainer],
  imports: [
    CommonModule
  ],
  exports: [],
  bootstrap: [NgbdToastGlobal]
})
export class ToastModule {
}
