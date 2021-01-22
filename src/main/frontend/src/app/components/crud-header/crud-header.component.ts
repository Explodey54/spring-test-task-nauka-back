import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import {Routes} from "@angular/router";
import {crudRoutes} from "../../crud-routes";

@Component({
  selector: 'crud-header',
  templateUrl: './crud-header.component.html',
  styleUrls: ['./crud-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudHeaderComponent implements OnInit {
  routes: Routes = crudRoutes;


  constructor() { }

  ngOnInit(): void {
  }

}
