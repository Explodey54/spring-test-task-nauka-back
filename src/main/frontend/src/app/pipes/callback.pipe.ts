import { PipeTransform, Pipe } from '@angular/core';

@Pipe({
  name: 'callback',
  pure: true
})
export class CallbackPipe<T> implements PipeTransform {
  transform(items: T[], callback: (items: T[]) => T[]): any {
    if (!items || !callback) {
      return items;
    }
    return callback(items);
  }
}
