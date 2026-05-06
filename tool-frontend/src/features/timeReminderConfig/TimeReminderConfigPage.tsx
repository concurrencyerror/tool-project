import {FormEvent, useCallback, useEffect, useState} from 'react';
import {
    createReminderConfig,
    deleteReminderConfig,
    fetchReminderConfigs,
    updateReminderConfig,
} from '../../api/reminderConfig';
import type {ReminderConfig, ReminderConfigFormValues} from '../../types/reminderConfig';
import ReminderConfigTable from './ReminderConfigTable';
import './TimeReminderConfigPage.css';

type TimeReminderConfigPageProps = {
    reloadKey: number;
};

const PAGE_SIZE = 10;

const toDatetimeLocalValue = (value: string) => {
    if (!value || value === '-') {
        return '';
    }

    return value.replace(' ', 'T').slice(0, 16);
};

const getInitialFormValues = (): ReminderConfigFormValues => ({
    reminderStartTime: '',
    reminderEndTime: '',
});

function TimeReminderConfigPage({reloadKey}: TimeReminderConfigPageProps) {
    const [reminderConfigs, setReminderConfigs] = useState<ReminderConfig[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isSaving, setIsSaving] = useState(false);
    const [deletingId, setDeletingId] = useState<string | number | null>(null);
    const [message, setMessage] = useState('');
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [total, setTotal] = useState(0);
    const [editingId, setEditingId] = useState<string | number | null>(null);
    const [formValues, setFormValues] = useState<ReminderConfigFormValues>(getInitialFormValues);

    const loadReminderConfigs = useCallback(async (targetPage = page) => {
        setMessage('');
        setIsLoading(true);

        try {
            const result = await fetchReminderConfigs({
                page: targetPage,
                size: PAGE_SIZE,
            });
            setReminderConfigs(result.items);
            setPage(result.page);
            setTotalPages(result.totalPages);
            setTotal(result.total);
        } catch (error) {
            console.error(error);
            setReminderConfigs([]);
            setMessage(error instanceof Error ? error.message : 'Reminder config load failed');
        } finally {
            setIsLoading(false);
        }
    }, [page]);

    useEffect(() => {
        void loadReminderConfigs(page);
    }, [loadReminderConfigs, page, reloadKey]);

    const resetForm = () => {
        setEditingId(null);
        setFormValues(getInitialFormValues());
    };

    const handleChange = (key: keyof ReminderConfigFormValues, value: string) => {
        setFormValues((current) => ({
            ...current,
            [key]: value,
        }));
    };

    const handleEdit = (item: ReminderConfig) => {
        setEditingId(item.id);
        setFormValues({
            reminderStartTime: toDatetimeLocalValue(item.reminderStartTime),
            reminderEndTime: toDatetimeLocalValue(item.reminderEndTime),
        });
    };

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setMessage('');

        if (!formValues.reminderStartTime || !formValues.reminderEndTime) {
            setMessage('Please complete all datetime fields');
            return;
        }

        setIsSaving(true);

        try {
            if (editingId === null) {
                await createReminderConfig(formValues);
                resetForm();
                await loadReminderConfigs(1);
            } else {
                await updateReminderConfig(editingId, formValues);
                resetForm();
                await loadReminderConfigs(page);
            }
        } catch (error) {
            console.error(error);
            setMessage(error instanceof Error ? error.message : 'Reminder config save failed');
        } finally {
            setIsSaving(false);
        }
    };

    const handleDelete = async (id: string | number) => {
        if (!window.confirm('Delete this reminder config?')) {
            return;
        }

        setMessage('');
        setDeletingId(id);

        try {
            await deleteReminderConfig(id);
            const nextPage = reminderConfigs.length === 1 && page > 1 ? page - 1 : page;
            await loadReminderConfigs(nextPage);

            if (editingId === id) {
                resetForm();
            }
        } catch (error) {
            console.error(error);
            setMessage(error instanceof Error ? error.message : 'Reminder config delete failed');
        } finally {
            setDeletingId(null);
        }
    };

    const canGoPrevious = page > 1 && !isLoading;
    const canGoNext = totalPages > 0 && page < totalPages && !isLoading;

    return (
        <>
            <header className="content-header">
                <div>
                    <p className="eyebrow">Config center</p>
                    <h2>时间配置提醒</h2>
                </div>
                <div className="header-actions">
                    <button className="refresh-button" type="button" onClick={() => loadReminderConfigs(page)}
                            disabled={isLoading}>
                        {isLoading ? 'Loading...' : 'Refresh'}
                    </button>
                </div>
            </header>

            <form className="config-form" onSubmit={handleSubmit}>
                <label>
                    <span>Reminder start</span>
                    <input
                        type="datetime-local"
                        value={formValues.reminderStartTime}
                        onChange={(event) => handleChange('reminderStartTime', event.target.value)}
                    />
                </label>
                <label>
                    <span>Reminder end</span>
                    <input
                        type="datetime-local"
                        value={formValues.reminderEndTime}
                        onChange={(event) => handleChange('reminderEndTime', event.target.value)}
                    />
                </label>
                <div className="form-actions">
                    <button className="refresh-button" type="submit" disabled={isSaving}>
                        {isSaving ? 'Saving...' : editingId === null ? 'Create' : 'Update'}
                    </button>
                    {editingId !== null ? (
                        <button className="secondary-button" type="button" onClick={resetForm}>
                            Cancel
                        </button>
                    ) : null}
                </div>
            </form>

            {message ? <p className="page-message">{message}</p> : null}

            <ReminderConfigTable
                data={reminderConfigs}
                isLoading={isLoading}
                message={message}
                deletingId={deletingId}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />

            <footer className="pagination-bar">
                <span>
                    Page {totalPages === 0 ? 0 : page} / {totalPages} | Total {total}
                </span>
                <div className="pagination-actions">
                    <button type="button" onClick={() => setPage((current) => Math.max(1, current - 1))}
                            disabled={!canGoPrevious}>
                        Previous
                    </button>
                    <button type="button" onClick={() => setPage((current) => current + 1)} disabled={!canGoNext}>
                        Next
                    </button>
                </div>
            </footer>
        </>
    );
}

export default TimeReminderConfigPage;
