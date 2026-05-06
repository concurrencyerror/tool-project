import {normalizeReminderConfigs} from '../features/timeReminderConfig/normalizeReminderConfigs';
import type {ReminderConfig, ReminderConfigFormValues, ReminderConfigPage} from '../types/reminderConfig';
import {REMINDER_CONFIG_API_URL} from './config';
import {assertApiSuccess, isObject, readJson} from './http';

type FetchReminderConfigParams = {
    page?: number;
    size?: number;
};

const readNumber = (source: Record<string, unknown>, keys: string[], fallback: number) => {
    for (const key of keys) {
        const value = source[key];

        if (typeof value === 'number' && Number.isFinite(value)) {
            return value;
        }

        if (typeof value === 'string') {
            const parsed = Number(value);

            if (Number.isFinite(parsed)) {
                return parsed;
            }
        }
    }

    return fallback;
};

const getResponseData = (payload: unknown) => {
    if (isObject(payload) && isObject(payload.data)) {
        return payload.data;
    }

    return payload;
};

const normalizePage = (payload: unknown, requestedPage: number, requestedSize: number): ReminderConfigPage => {
    const data = getResponseData(payload);
    const items = normalizeReminderConfigs(payload);

    if (!isObject(data)) {
        return {
            items,
            page: requestedPage,
            size: requestedSize,
            total: items.length,
            totalPages: items.length > 0 ? 1 : 0,
        };
    }

    const total = readNumber(data, ['totalElements', 'total', 'totalCount'], items.length);
    const totalPages = readNumber(data, ['totalPages', 'pages'], Math.ceil(total / requestedSize));
    const zeroBasedPage = readNumber(data, ['number', 'pageNumber'], requestedPage - 1);
    const size = readNumber(data, ['size', 'pageSize'], requestedSize);

    return {
        items,
        page: zeroBasedPage + 1,
        size,
        total,
        totalPages,
    };
};

const toRequestBody = (values: ReminderConfigFormValues) => ({
    remindStartTime: values.reminderStartTime,
    remindEndTime: values.reminderEndTime,
});

export const fetchReminderConfigs = async ({
                                               page = 1,
                                               size = 10,
                                           }: FetchReminderConfigParams = {}): Promise<ReminderConfigPage> => {
    const params = new URLSearchParams({
        page: String(page),
        size: String(size),
    });
    const response = await fetch(`${REMINDER_CONFIG_API_URL}?${params.toString()}`, {
        credentials: 'include',
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, 'Reminder config load failed');

    return normalizePage(payload, page, size);
};

export const fetchReminderConfigById = async (id: string | number): Promise<ReminderConfig | null> => {
    const response = await fetch(`${REMINDER_CONFIG_API_URL}/${id}`, {
        credentials: 'include',
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, 'Reminder config load failed');

    const items = normalizeReminderConfigs(payload);
    return items[0] ?? null;
};

export const createReminderConfig = async (values: ReminderConfigFormValues): Promise<void> => {
    const response = await fetch(REMINDER_CONFIG_API_URL, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(toRequestBody(values)),
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, 'Reminder config create failed');
};

export const updateReminderConfig = async (
    id: string | number,
    values: ReminderConfigFormValues,
): Promise<void> => {
    const response = await fetch(`${REMINDER_CONFIG_API_URL}/${id}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(toRequestBody(values)),
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, 'Reminder config update failed');
};

export const deleteReminderConfig = async (id: string | number): Promise<void> => {
    const response = await fetch(`${REMINDER_CONFIG_API_URL}/${id}`, {
        method: 'DELETE',
        credentials: 'include',
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, 'Reminder config delete failed');
};
