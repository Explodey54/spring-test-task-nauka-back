import { Pipe, PipeTransform } from '@angular/core';
import { get } from 'lodash';

@Pipe({
  name: 'property'
})
export class PropertyPipe implements PipeTransform {

  transform(value: any, path: string): unknown {
    return get(value, path);
  }
}
