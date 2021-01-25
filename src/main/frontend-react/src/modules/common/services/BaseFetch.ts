import Auth from "./Auth";
import config from "../../../config";

const createFullUrl = (url: string): string => {
    const baseApi = config.baseApi;
    return baseApi + url;
}

const BaseFetch = {
    fetch: (url: string, config?: RequestInit): Promise<Response> => {
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
        return fetch(createFullUrl(url), config);
    }
};

export default BaseFetch;
