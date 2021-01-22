import { BrowserModule } from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import { CrudViewerComponent } from './components/crud-viewer/crud-viewer.component';
import { CrudViewerRowComponent } from './components/crud-viewer-row/crud-viewer-row.component';
import { PropertyPipe } from './pipes/property.pipe';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AppRoutingModule} from "./app-routing.module";
import { CrudViewContainerComponent } from './containers/crud-view-container/crud-view-container.component';
import { LoginContainerComponent } from './containers/login-container/login-container.component';
import { LoginFormComponent } from './components/login-form/login-form.component';
import {ReactiveFormsModule} from "@angular/forms";
import {BaseInterceptor} from "./http/BaseInterceptor";
import {AppComponent} from "./app.component";
import { CrudViewerRowHeadComponent } from './components/crud-viewer-row-head/crud-viewer-row-head.component';
import { CrudFormComponent } from './components/crud-form/crud-form.component';
import { AutocompleteSelectComponent } from './components/autocomplete-select/autocomplete-select.component';
import { CrudHeaderComponent } from './components/crud-header/crud-header.component';
import { CrudFilterComponent } from './components/crud-filter/crud-filter.component';
import {CallbackPipe} from "./pipes/callback.pipe";

@NgModule({
  declarations: [
    AppComponent,
    CrudViewerComponent,
    CrudViewerRowComponent,
    PropertyPipe,
    CallbackPipe,
    CrudViewContainerComponent,
    LoginContainerComponent,
    LoginFormComponent,
    CrudViewerRowHeadComponent,
    CrudFormComponent,
    AutocompleteSelectComponent,
    CrudHeaderComponent,
    CrudFilterComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BaseInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
