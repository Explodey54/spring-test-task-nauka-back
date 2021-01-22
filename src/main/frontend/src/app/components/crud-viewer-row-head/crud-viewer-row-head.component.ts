import {Component, OnInit, ChangeDetectionStrategy, Input} from '@angular/core';
import {ICrudViewerConfiguration, ICrudViewerConfigurationField} from "../../types/CrudViewerConfiguration";

@Component({
  selector: 'crud-viewer-row-head',
  templateUrl: './crud-viewer-row-head.component.html',
  styleUrls: ['./crud-viewer-row-head.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudViewerRowHeadComponent implements OnInit {

  @Input() config: ICrudViewerConfiguration | undefined;

  constructor() { }

  ngOnInit(): void {
  }

  filterReadableFields(fields: ICrudViewerConfigurationField[]): ICrudViewerConfigurationField[] {
    return fields.filter(i => !i.writeOnly);
  }

}
