import {useCallback, useEffect, useState} from 'react';
import {fetchReminderConfigs} from '../../api/reminderConfig';
import type {ReminderConfig} from '../../types/reminderConfig';
import ReminderConfigTable from './ReminderConfigTable';
import './TimeReminderConfigPage.css';

type TimeReminderConfigPageProps = {
    reloadKey: number;
};

function TimeReminderConfigPage({reloadKey}: TimeReminderConfigPageProps) {
    const [reminderConfigs, setReminderConfigs] = useState<ReminderConfig[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState('');

    const loadReminderConfigs = useCallback(async () => {
        setMessage('');
        setIsLoading(true);

        try {
            const list = await fetchReminderConfigs();
            setReminderConfigs(list);
        } catch (error) {
            console.error(error);
            setReminderConfigs([]);
            setMessage(error instanceof Error ? error.message : '时间提醒配置加载失败');
        } finally {
            setIsLoading(false);
        }
    }, []);

    useEffect(() => {
        void loadReminderConfigs();
    }, [loadReminderConfigs, reloadKey]);

    return (
        <>
            <header className="content-header">
                <div>
                    <p className="eyebrow">配置中心</p>
                    <h2>时间提醒配置</h2>
                </div>
                <button className="refresh-button" type="button" onClick={loadReminderConfigs} disabled={isLoading}>
                    {isLoading ? '加载中...' : '刷新列表'}
                </button>
            </header>

            <ReminderConfigTable data={reminderConfigs} isLoading={isLoading} message={message}/>
        </>
    );
}

export default TimeReminderConfigPage;
