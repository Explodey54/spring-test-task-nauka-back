import {Component, OnInit, ChangeDetectionStrategy, OnDestroy} from '@angular/core';
import {ActivatedRoute,} from "@angular/router";
import {CrudApiService} from "../../service/crud-api/crud-api.service";
import {BehaviorSubject, Subject, timer} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {Dictionary} from "../../types/Dictionary";
import {BaseHttpResponseError} from "../../types/BaseHttpResponse";
import {getIdKey, ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";

enum CrudFormAction {
  Create, Edit
}

@Component({
  selector: 'crud-view-container',
  templateUrl: './crud-view-container.component.html',
  styleUrls: ['./crud-view-container.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrudViewContainerComponent<T> implements OnInit, OnDestroy {
  crudConfig: ICrudViewerConfiguration;

  entityList$ = new BehaviorSubject<T[]>([]);
  showForm$ = new BehaviorSubject(false);
  formEntity$ = new BehaviorSubject<T | null>(null);
  error$ = new BehaviorSubject<string | null>(null);

  get baseApi(): string | null {
    return this.crudConfig?.baseApi;
  }

  private destroyed$ = new Subject();
  private formAction: CrudFormAction = CrudFormAction.Create;
  private filter: Dictionary;

  constructor(private route: ActivatedRoute, private crudApi: CrudApiService) {
    this.crudConfig = route.snapshot.data.crudConfig;
    this.filter = this.createFilter(this.crudConfig);
  }

  ngOnInit() {
    this.fetchGetAll();
  }

  ngOnDestroy() {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  onFormSubmit(value: T) {
    switch (this.formAction) {
      case CrudFormAction.Create: {
        this.create(value);
        break;
      }
      case CrudFormAction.Edit: {
        const formEntity = this.formEntity$.value;
        if (!formEntity) {
          throw new Error("Form entity doesn't exist!")
        }
        const id = this.getEntityId(formEntity, this.crudConfig);
        this.edit(value, id);
        break;
      }
      default: {
        console.warn("CrudFormAction not set!")
      }
    }
  }

  onCreate() {
    this.formAction = CrudFormAction.Create;
    this.formEntity$.next(null);
    this.showForm$.next(true);
  }

  onEdit(entity: T) {
    this.formAction = CrudFormAction.Edit;
    this.formEntity$.next(entity);
    this.showForm$.next(true);
  }

  onDelete(entity: T) {
    this.delete(entity);
  }

  onFilterSubmit(filter: Dictionary) {
    this.filter = filter;
    this.fetchGetAll();
  }

  private fetchGetAll() {
    if (!this.baseApi) { return; }
    this.crudApi.getAll<T>(this.baseApi, this.prepareFilter(this.filter))
      .pipe(takeUntil(this.destroyed$))
      .subscribe(i => this.entityList$.next(i));
  }

  private create(entity: T) {
    if (!this.baseApi) { return; }
    this.crudApi.create(this.baseApi, entity)
      .pipe(takeUntil(this.destroyed$))
      .subscribe(
        () => {
          this.closeForm();
          this.closeError();
          this.fetchGetAll();
        },
        ({error}: BaseHttpResponseError) => this.showError(error.reason)
      );
  }

  private edit(entity: T, id: number) {
    if (!this.baseApi) { return; }
    this.crudApi.update(this.baseApi, entity, id)
      .pipe(takeUntil(this.destroyed$))
      .subscribe(
        () => {
          this.closeForm();
          this.closeError();
          this.fetchGetAll();
        },
        ({error}: BaseHttpResponseError) => this.showError(error.reason)
      );
  }

  private delete(entity: T) {
    if (!this.baseApi) { return; }
    const id = this.getEntityId(entity, this.crudConfig);
    this.crudApi.delete(this.baseApi, id)
      .pipe(takeUntil(this.destroyed$))
      .subscribe(
        () => this.fetchGetAll(),
        ({error}: BaseHttpResponseError) => this.showError(error.reason)
      );
  }

  private closeForm() {
    this.showForm$.next(false);
    this.formEntity$.next(null);
  }

  private showError(error: string, timeout = 9000) {
    this.error$.next(error);
    timer(timeout)
      .pipe(takeUntil(this.destroyed$))
      .subscribe(() => this.error$.next(null));
  }

  private closeError() {
    this.error$.next(null);
  }

  private getEntityId(entity: Dictionary, config: ICrudViewerConfiguration): number {
    const idKey = getIdKey(config);
    const id = Number(entity[idKey]);
    if (!id) {
      throw new Error(`Id (${entity[idKey]}) is not a number!`);
    }
    return id;
  }

  private createFilter(config: ICrudViewerConfiguration): Dictionary {
    const output: Dictionary = {};
    config.filterFields?.forEach(i => {
      if (i.default) output[i.path] = i.default;
    });
    return output;
  }

  private prepareFilter(filter: Dictionary): Dictionary {
    const output: Dictionary = {};
    Object.entries(filter).forEach(([key, value]) => {
      if (value) output[key] = value;
    });
    return output;
  }
}
