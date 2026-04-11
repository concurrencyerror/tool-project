import './Sidebar.css';
import {useState} from 'react';
import {logout} from '../../api/auth';

type SidebarProps = {
    activeMenu: string;
    onSelectTimeReminderConfig: () => void;
    onLogoutSuccess: () => void;
};

function Sidebar({activeMenu, onSelectTimeReminderConfig, onLogoutSuccess}: SidebarProps) {
    const [isLoggingOut, setIsLoggingOut] = useState(false);

    const handleLogout = async () => {
        setIsLoggingOut(true);

        try {
            await logout();
            onLogoutSuccess();
        } catch (error) {
            console.error(error);
            window.alert(error instanceof Error ? error.message : '退出失败，请稍后再试');
        } finally {
            setIsLoggingOut(false);
        }
    };

    return (
        <aside className="sidebar">
            <div>
                <h1 className="dashboard-brand">工具管理台</h1>
                <button
                    className={activeMenu === 'time-reminder-config' ? 'menu-item active' : 'menu-item'}
                    type="button"
                    onClick={onSelectTimeReminderConfig}
                >
                    时间提醒配置
                </button>
            </div>

            <button className="logout-button" type="button" onClick={handleLogout} disabled={isLoggingOut}>
                {isLoggingOut ? '退出中...' : '退出登录'}
            </button>
        </aside>
    );
}

export default Sidebar;
