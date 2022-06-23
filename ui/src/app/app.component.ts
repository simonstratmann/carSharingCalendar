import {Component} from "@angular/core";
import flatpickr from "flatpickr";
import {German} from "flatpickr/dist/l10n/de";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  title = "ui";

  username: string;

  constructor() {
    flatpickr.localize(German);

  }


}
