import {AUTH_ME_API_URL, LOGIN_API_URL, LOGOUT_API_URL} from './config';
import {assertApiSuccess, isFailedApiResult, readJson} from './http';

export const login = async (username: string, password: string) => {
    const response = await fetch(LOGIN_API_URL, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username,
            password,
        }),
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, '登录失败，请检查用户名或密码');
};

export const logout = async () => {
    const response = await fetch(LOGOUT_API_URL, {
        method: 'POST',
        credentials: 'include',
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, '退出失败，请稍后再试');
};

export const checkLoginStatus = async () => {
    const response = await fetch(AUTH_ME_API_URL, {
        credentials: 'include',
    });
    const payload = await readJson(response);

    return !isFailedApiResult(response, payload);
};
