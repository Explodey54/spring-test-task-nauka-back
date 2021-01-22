import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BaseHttpResponseSuccess} from "../../types/BaseHttpResponse";
import {map} from "rxjs/operators";
import {Dictionary} from "../../types/Dictionary";

@Injectable({
  providedIn: 'root'
})
export class CrudApiService {

  constructor(private http: HttpClient) {}

  getAll<T>(path: string, params?: Dictionary): Observable<T[]> {
    const baseResponse = this.http.get<BaseHttpResponseSuccess<T[]>>(path, { params });
    return baseResponse.pipe(map(i => i.data));
  }

  getById<T>(path: string, id: number): Observable<T> {
    const baseResponse = this.http.get<BaseHttpResponseSuccess<T>>(`${path}/${id}`);
    return baseResponse.pipe(map(i => i.data));
  }

  create<T>(path: string, body: T): Observable<T> {
    const baseResponse = this.http.post<BaseHttpResponseSuccess<T>>(path, body);
    return baseResponse.pipe(map(i => i.data));
  }

  update<T>(path: string, body: T, id: number): Observable<T> {
    const baseResponse = this.http.put<BaseHttpResponseSuccess<T>>(`${path}/${id}`, body);
    return baseResponse.pipe(map(i => i.data));
  }

  delete<T>(path: string, id: number): Observable<void> {
    const baseResponse = this.http.delete<BaseHttpResponseSuccess<T[]>>(`${path}/${id}`);
    return baseResponse.pipe(map(() => {}));
  }
}
