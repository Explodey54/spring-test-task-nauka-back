import {Component, ChangeDetectionStrategy, Input, Output, EventEmitter, Pipe, OnInit} from '@angular/core';
import {ICrudViewerConfiguration, ICrudViewerConfigurationField} from "../../types/CrudViewerConfiguration";

@Component({
  selector: 'crud-viewer-row',
  templateUrl: './crud-viewer-row.component.html',
  styleUrls: ['./crud-viewer-row.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudViewerRowComponent<T> {
  @Input() entity: T | undefined;
  @Input() config: ICrudViewerConfiguration | undefined;
  @Output() edit = new EventEmitter<T>();
  @Output() delete = new EventEmitter<T>();

  onEdit() {
    this.edit.emit(this.entity);
  }

  onDelete() {
    this.delete.emit(this.entity);
  }

  filterReadableFields(fields: ICrudViewerConfigurationField[]): ICrudViewerConfigurationField[] {
    return fields.filter(i => !i.writeOnly);
  }
}
