import {getListCandidate, isObject} from '../../api/http';
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

export const normalizeReminderConfigs = (payload: unknown): ReminderConfig[] => {
    const list = getListCandidate(payload);

    if (!Array.isArray(list)) {
        return [];
    }

    return list.filter(isObject).map((item, index) => {
        const id = readText(item, ['id', 'configId', 'reminderConfigId']);

        return {
            id: id === '-' ? index : id,
            reminderStartTime: readText(item, ['reminderStartTime', 'remindStartTime', 'startReminderTime', '提醒的开始时间']),
            reminderEndTime: readText(item, ['reminderEndTime', 'remindEndTime', 'endReminderTime', '提醒的结束时间']),
            configStartTime: readText(item, ['configStartTime', 'settingStartTime', '配置的开始时间']),
        };
    });
};
