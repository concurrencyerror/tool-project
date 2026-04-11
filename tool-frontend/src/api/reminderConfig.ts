import {normalizeReminderConfigs} from '../features/timeReminderConfig/normalizeReminderConfigs';
import type {ReminderConfig} from '../types/reminderConfig';
import {REMINDER_CONFIG_LIST_API_URL} from './config';
import {assertApiSuccess, readJson} from './http';

export const fetchReminderConfigs = async (): Promise<ReminderConfig[]> => {
    const response = await fetch(REMINDER_CONFIG_LIST_API_URL, {
        credentials: 'include',
    });
    const payload = await readJson(response);

    assertApiSuccess(response, payload, '时间提醒配置加载失败');

    return normalizeReminderConfigs(payload);
};
