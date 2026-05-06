import {isObject} from '../../api/http';
import type {ReminderConfig} from '../../types/reminderConfig';

const readText = (source: Record<string, unknown>, keys: string[]) => {
    for (const key of keys) {
        const value = source[key];

        if (typeof value === 'string' || typeof value === 'number') {
            return String(value);
        }
    }

    return '-';
};

export const normalizeReminderConfig = (
    source: Record<string, unknown>,
    fallbackId: string | number,
): ReminderConfig => {
    const id = readText(source, ['id', 'configId', 'reminderConfigId']);

    return {
        id: id === '-' ? fallbackId : id,
        reminderStartTime: readText(source, ['reminderStartTime', 'remindStartTime', 'startReminderTime']),
        reminderEndTime: readText(source, ['reminderEndTime', 'remindEndTime', 'endReminderTime']),
        configStartTime: readText(source, ['configStartTime', 'createTime', 'settingStartTime']),
    };
};

export const normalizeReminderConfigs = (payload: unknown): ReminderConfig[] => {
    if (Array.isArray(payload)) {
        return payload.filter(isObject).map((item, index) => normalizeReminderConfig(item, index));
    }

    const data = isObject(payload) ? payload.data : undefined;

    if (Array.isArray(data)) {
        return data.filter(isObject).map((item, index) => normalizeReminderConfig(item, index));
    }

    if (isObject(data) && Array.isArray(data.content)) {
        return data.content.filter(isObject).map((item, index) => normalizeReminderConfig(item, index));
    }

    if (isObject(payload) && Array.isArray(payload.content)) {
        return payload.content.filter(isObject).map((item, index) => normalizeReminderConfig(item, index));
    }

    if (isObject(data)) {
        return [normalizeReminderConfig(data, 0)];
    }

    if (isObject(payload)) {
        return [normalizeReminderConfig(payload, 0)];
    }

    return [];
};
