import Auth from "./Auth";
import config from "../../../config";
import {HttpResponseSuccess, HttpResponseValue} from "../types/HttpResponse";

const createFullUrl = (url: string): string => {
    const baseApi = config.baseApi;
    return `${baseApi}/${url}`;
}

const fetch = (url: string, config?: RequestInit): Promise<Response> => {
    const token = Auth.getToken();
    if (!config) config = {};

    config.headers = {
        ...config.headers,
        'Content-Type': 'application/json'
    }

    if (token) {
        config.headers = {
            ...config.headers,
            Authorization: `Bearer ${token}`
        }
    }
    return window.fetch(createFullUrl(url), config);
}

const fetchJson = <T = any>(url: string, config?: RequestInit): Promise<HttpResponseValue<T>> => {
    return fetch(url, config)
        .then(i => {
            if (i.status === 403) {
                Auth.logout();
                throw new Error(i.statusText);
            }
            return i;
        })
        .then(i => i.json())
        .catch(i => console.log);
}

const BaseFetch = { fetch, fetchJson };

export default BaseFetch;
