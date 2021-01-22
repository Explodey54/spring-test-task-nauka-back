import {Component, OnInit, ChangeDetectionStrategy, Input, forwardRef} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";
import {BaseHttpResponseError, BaseHttpResponseSuccess} from "../../types/BaseHttpResponse";
import {ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR} from "@angular/forms";

export interface IAutocomplete {
  id: number;
  title: string;
}

@Component({
  selector: 'autocomplete-select',
  templateUrl: './autocomplete-select.component.html',
  styleUrls: ['./autocomplete-select.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => AutocompleteSelectComponent),
    multi: true
  }]
})
export class AutocompleteSelectComponent implements OnInit, ControlValueAccessor {
  @Input() endpoint: string | undefined;
  @Input()
  set value(val: number | null) {
    this.select.setValue(val);
    this.onChange(val);
  }

  get value() {
    return this.select.value;
  }

  select = new FormControl();

  isLoading$ = new BehaviorSubject(true);
  autocompleteList$ = new BehaviorSubject<IAutocomplete[]>([]);
  error$ = new BehaviorSubject('');

  onChange: (val: any) => void = () => {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchAutocompleteList();
    this.select.valueChanges.subscribe(i => this.onChange(i));
  }

  fetchAutocompleteList(): void {
    if (!this.endpoint) {
      this.error$.next("Endpoint is not set!");
      this.isLoading$.next(false);
      return;
    }
    this.getAutocompleteList(this.endpoint).subscribe(
      resp => {
        this.autocompleteList$.next(resp);
        this.isLoading$.next(false);
      },
      ({error}: BaseHttpResponseError) => {
        this.error$.next(error.reason);
        this.isLoading$.next(false);
      }
    );
  }

  writeValue(value: any): void {
    this.select.setValue(value);
    this.onChange(value);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
  }

  private getAutocompleteList(endpoint: string): Observable<IAutocomplete[]> {
    return this.http.get<IAutocomplete[]>(`autocomplete/${endpoint}`);
  }
}
