export type ReminderConfig = {
    id: string | number;
    reminderStartTime: string;
    reminderEndTime: string;
    configStartTime: string;
};

export type ReminderConfigFormValues = {
    reminderStartTime: string;
    reminderEndTime: string;
};

export type ReminderConfigPage = {
    items: ReminderConfig[];
    page: number;
    size: number;
    total: number;
    totalPages: number;
};
