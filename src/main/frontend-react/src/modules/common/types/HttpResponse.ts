export interface HttpResponse {
    ok: boolean;
}

export interface HttpResponseSuccess<T = any> extends HttpResponse {
    data: T;
}

export interface HttpResponseError extends HttpResponse {
    reason: string;
}

export type HttpResponseValue<T> = HttpResponseSuccess<T> & HttpResponseError;
