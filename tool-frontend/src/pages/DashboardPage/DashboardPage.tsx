import {useState} from 'react';
import Sidebar from '../../components/Sidebar/Sidebar';
import TimeReminderConfigPage from '../../features/timeReminderConfig/TimeReminderConfigPage';
import './DashboardPage.css';

type MenuKey = 'time-reminder-config' | '';

type DashboardPageProps = {
    onLogoutSuccess: () => void;
};

function DashboardPage({onLogoutSuccess}: DashboardPageProps) {
    const [activeMenu, setActiveMenu] = useState<MenuKey>('');
    const [reloadKey, setReloadKey] = useState(0);

    const handleSelectTimeReminderConfig = () => {
        setActiveMenu('time-reminder-config');
        setReloadKey((key) => key + 1);
    };

    return (
        <main className="dashboard-page">
            <Sidebar
                activeMenu={activeMenu}
                onSelectTimeReminderConfig={handleSelectTimeReminderConfig}
                onLogoutSuccess={onLogoutSuccess}
            />

            <section className="content-area">
                {activeMenu === 'time-reminder-config' ? (
                    <TimeReminderConfigPage reloadKey={reloadKey}/>
                ) : (
                    <div className="empty-state">请选择左侧配置项</div>
                )}
            </section>
        </main>
    );
}

export default DashboardPage;
