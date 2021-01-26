import {Storage} from "./Storage";
import {IAuthForm} from "../../auth/components/AuthForm/AuthForm";
import BaseFetch from "./BaseFetch";

const TOKEN_STORAGE_KEY = 'token';

const setToken = (token: string) => Storage.set(TOKEN_STORAGE_KEY, token);
const getToken = () => Storage.get(TOKEN_STORAGE_KEY);
const isLoggedIn = () => !!getToken();
const login = async (value: IAuthForm): Promise<any> => {
    const resp = await BaseFetch.fetch('auth', { method: 'POST', body: JSON.stringify(value)})
        .then(res => res.json());
    if (resp.ok) {
        setToken(resp?.data?.token);
    }
    return resp;
}
const logout = () => {
    Storage.remove(TOKEN_STORAGE_KEY);
};

const Auth = { login, logout, isLoggedIn, getToken };

export default Auth;
