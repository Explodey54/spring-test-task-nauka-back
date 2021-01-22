import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {IFormBuilder, IFormGroup} from "@rxweb/types";
import {FormBuilder, Validators} from "@angular/forms";

export interface LoginForm {
  username: string;
  password: string;
}

@Component({
  selector: 'login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginFormComponent {
  @Input() error: string | null = '';
  @Output() submitForm = new EventEmitter<LoginForm>();

  formGroup: IFormGroup<LoginForm>;
  private formBuilder: IFormBuilder = new FormBuilder();

  constructor() {
    this.formGroup = this.formBuilder.group<LoginForm>({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    const value = this.formGroup.value;
    if (this.formGroup.valid && value) {
      this.submitForm.emit(value);
    }
  }
}
