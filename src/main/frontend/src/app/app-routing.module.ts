import {NgModule} from '@angular/core';
import {Route, RouterModule, Routes} from '@angular/router';
import {LoginContainerComponent} from "./containers/login-container/login-container.component";
import {ICrudViewerConfiguration} from "./types/CrudViewerConfiguration";
import {crudRoutes} from "./crud-routes";

export interface RouteWithCrudConfig extends Route {
  data?: {
    crudConfig: ICrudViewerConfiguration;
  }
}

export const routes: Routes & RouteWithCrudConfig[] = [
  { path: '',   redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    component: LoginContainerComponent
  },
  ...crudRoutes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
