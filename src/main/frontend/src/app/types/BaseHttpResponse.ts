import {HttpErrorResponse} from "@angular/common/http";

export interface BaseHttpResponseSuccess<T> {
  ok: boolean;
  data: T;
}

export interface BaseHttpResponseError extends HttpErrorResponse {
  error: {
    reason: string;
    fields: {[key: string]: BaseHttpResponseErrorField};
  };
}

export type BaseHttpResponseErrorField = string[];
