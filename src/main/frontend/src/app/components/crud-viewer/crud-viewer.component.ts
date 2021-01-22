import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";

@Component({
  selector: 'crud-viewer',
  templateUrl: './crud-viewer.component.html',
  styleUrls: ['./crud-viewer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudViewerComponent<T> {
  @Input() entityList: T[] | null = [];
  @Input() config: ICrudViewerConfiguration | undefined;
  @Output() edit = new EventEmitter<T>();
  @Output() delete = new EventEmitter<T>();

  onEdit(entity: T) {
    this.edit.emit(entity);
  }

  onDelete(entity: T) {
    this.delete.emit(entity);
  }
}
