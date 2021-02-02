import BaseFetch from "./BaseFetch";
import {HttpResponseError, HttpResponseSuccess} from "../types/HttpResponse";

const getAll = <T=any>(url: string) => {
    return BaseFetch.fetchJson(url);
}

const edit = <T=any>(url: string, id: number, body: Partial<T>) => {
    return BaseFetch.fetchJson<T>(`${url}/${id}`, {
        body: JSON.stringify(body),
        method: 'PUT'
    });
}

const remove = <T=any>(url: string, id: number) => {
    return BaseFetch.fetchJson(`${url}/${id}`, { method: 'DELETE' });
}

const create = <T=any>(url: string, body: Partial<T>) => {
    return BaseFetch.fetchJson(url, {
        body: JSON.stringify(body),
        method: 'POST'
    });
}

export default { getAll, edit, remove, create };
