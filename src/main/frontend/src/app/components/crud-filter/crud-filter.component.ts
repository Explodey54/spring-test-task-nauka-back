import {Component, OnInit, ChangeDetectionStrategy, Input, EventEmitter, Output} from '@angular/core';
import {
  ICrudViewerConfiguration,
  ICrudViewerConfigurationFilterField
} from "../../types/CrudViewerConfiguration";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Dictionary} from "../../types/Dictionary";

@Component({
  selector: 'crud-filter',
  templateUrl: './crud-filter.component.html',
  styleUrls: ['./crud-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudFilterComponent implements OnInit {

  @Input() config: ICrudViewerConfiguration | null = null;
  @Output() submitForm = new EventEmitter<object>();

  formGroup!: FormGroup;
  private fb = new FormBuilder();

  ngOnInit(): void {
    if (this.config?.filterFields) {
      this.formGroup = this.fb.group(this.createControlsFromConfig(this.config.filterFields));
    }
  }

  private createControlsFromConfig(config: ICrudViewerConfigurationFilterField[]): Dictionary {
    const output: {[key: string]: any} = {};
    config.forEach(i => {
      const value = i.default || '';
      const path = i.path;
      output[path] = [value];
    });
    return output;
  }

  onSubmit() {
    this.submitForm.emit(this.formGroup.value);
  }
}
