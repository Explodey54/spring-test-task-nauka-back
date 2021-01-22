import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {StorageService} from "../service/storage/storage.service";

/** Add the api from environment to http request url. */
@Injectable()
export class BaseInterceptor implements HttpInterceptor {

  constructor(private storage: StorageService) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req.clone({
      setHeaders: {
        Authorization: `Bearer ${this.getToken()}`
      },
      url: environment.api + this.processUrl(req.url)
    }));
  }

  private processUrl(url: string) {
    if (url.charAt(0) === "/") {
      url = url.substring(1);
    }
    return url;
  }

  private getToken(): string | null {
    return this.storage.get("token");
  }
}
