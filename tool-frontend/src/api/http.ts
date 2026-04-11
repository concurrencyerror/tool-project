const SUCCESS_CODES = [0, 200, '0', '200', 'SUCCESS', 'success'];

export const isObject = (value: unknown): value is Record<string, unknown> =>
    typeof value === 'object' && value !== null;

export const readJson = async (response: Response): Promise<unknown> => {
    const text = await response.text();

    if (!text) {
        return null;
    }

    try {
        return JSON.parse(text);
    } catch {
        return null;
    }
};

export const getApiMessage = (payload: unknown, fallback: string) => {
    if (!isObject(payload)) {
        return fallback;
    }

    if (typeof payload.message === 'string') {
        return payload.message;
    }

    if (typeof payload.msg === 'string') {
        return payload.msg;
    }

    return fallback;
};

export const isFailedApiResult = (response: Response, payload: unknown) => {
    if (!response.ok) {
        return true;
    }

    if (!isObject(payload)) {
        return false;
    }

    if (typeof payload.success === 'boolean') {
        return !payload.success;
    }

    if (typeof payload.code === 'number' || typeof payload.code === 'string') {
        return !SUCCESS_CODES.includes(payload.code);
    }

    return false;
};

export const assertApiSuccess = (response: Response, payload: unknown, fallbackMessage: string) => {
    if (isFailedApiResult(response, payload)) {
        throw new Error(getApiMessage(payload, fallbackMessage));
    }
};

export const getListCandidate = (payload: unknown): unknown => {
    if (Array.isArray(payload)) {
        return payload;
    }

    if (!isObject(payload)) {
        return [];
    }

    if (Array.isArray(payload.data)) {
        return payload.data;
    }

    if (Array.isArray(payload.list)) {
        return payload.list;
    }

    if (Array.isArray(payload.records)) {
        return payload.records;
    }

    if (Array.isArray(payload.items)) {
        return payload.items;
    }

    if (isObject(payload.data)) {
        return getListCandidate(payload.data);
    }

    return [];
};
