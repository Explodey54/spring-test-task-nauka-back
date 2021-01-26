export interface HttpResponse {
    ok: boolean;
}

export interface HttpResponseSuccess<T> extends HttpResponse {
    data: T;
}

export interface HttpResponseError extends HttpResponse {
    reason: string;
}
