import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {StorageService} from "../storage/storage.service";
import {BaseHttpResponseSuccess} from "../../types/BaseHttpResponse";
import {LoginForm} from "../../components/login-form/login-form.component";

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private storage: StorageService) {
    this.token = storage.get("token");
    if (this.token) {
      this.isLoggedIn = true;
    }
  }

  isLoggedIn = false;
  token: string | null;

  login(form: LoginForm): Observable<LoginResponse> {
    return this.loginRequest(form).pipe(
      tap(i => this.onLoginSuccess(i.data), () => this.onLoginError()),
      map(i => i.data)
    );
  }

  private onLoginSuccess(resp: LoginResponse) {
    this.isLoggedIn = true;
    console.log(resp);
    this.token = resp.token;
    this.storage.set("token", resp.token);
  }

  private onLoginError() {
    this.isLoggedIn = false;
  }

  private loginRequest(form: LoginForm) {
    return this.http.post<BaseHttpResponseSuccess<LoginResponse>>("/auth", form);
  }
}
