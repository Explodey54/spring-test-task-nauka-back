import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {LoginForm} from "../../types/LoginForm";
import {AuthService} from "../../service/auth/auth.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'login-container',
  templateUrl: './login-container.component.html',
  styleUrls: ['./login-container.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginContainerComponent {
  formError = new BehaviorSubject<string>('');

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit(form: LoginForm) {
    this.formError.next('');
    this.auth.login(form).subscribe(
      () => {
        this.router.navigate(["departments"]);
      },
      ({error}: HttpErrorResponse) => {
        this.formError.next(error.reason);
      }
    );
  }
}
