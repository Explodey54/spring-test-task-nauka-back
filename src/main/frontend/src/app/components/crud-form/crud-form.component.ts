import {
  Component,
  ChangeDetectionStrategy,
  Input,
  SimpleChanges,
  OnChanges,
  Output,
  EventEmitter
} from '@angular/core';
import {
  ICrudViewerConfiguration,
  ICrudViewerConfigurationField
} from "../../types/CrudViewerConfiguration";
import {FormBuilder, FormGroup} from "@angular/forms";
import {IFormBuilder} from "@rxweb/types";
import {Dictionary} from "../../types/Dictionary";
import { get } from 'lodash';

@Component({
  selector: 'crud-form',
  templateUrl: './crud-form.component.html',
  styleUrls: ['./crud-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudFormComponent<T> implements OnChanges {

  @Input() config: ICrudViewerConfiguration | null = null;
  @Input() entity: T | null = null;
  @Output() submitForm = new EventEmitter();

  configEditableFields: ICrudViewerConfigurationField[] = [];
  formGroup!: FormGroup;

  private formBuilder: IFormBuilder = new FormBuilder();

  ngOnChanges({config, entity}: SimpleChanges) {
    if (config && config.isFirstChange() && this.config) {
      this.processConfig(this.config);
    }
    if (entity) {
      this.processEntity(this.entity);
    }
  }

  onSubmit() {
    const values = this.prepareFormValues(this.formGroup.value);
    this.submitForm.emit(values);
  }

  private processConfig(config: ICrudViewerConfiguration): void {
    this.configEditableFields = config.fields
      .filter(i => !i.readOnly)
      .filter(i => !i.isId)
      .filter(i => i.writePath || !i.path.includes("."));
    this.formGroup = this.formBuilder.group(this.createControlsFromConfigFields(this.configEditableFields));
  }

  private processEntity(entity: T | null) {
    if (!this.formGroup) { return; }
    if (entity) {
      this.configEditableFields.forEach(i => {
        const value = get(entity, i.path);
        this.formGroup.get(i.writePath || i.path)?.setValue(value);
      });
    } else {
      this.formGroup.reset();
    }
  }

  private createControlsFromConfigFields(config: ICrudViewerConfigurationField[]): Dictionary {
    const output: {[key: string]: any} = {};
    config.forEach(i => {
      const value = '';
      const path = i.writePath || i.path;
      if (!path) { return; }
      output[path] = [value];
    });
    return output;
  }

  private prepareFormValues(obj: Dictionary): object {
    Object.entries(obj).forEach(([key, value]) => {
      if (!value) {
        delete obj[key];
      }
    });
    return obj;
  }
}
