import type {ReminderConfig} from '../../types/reminderConfig';

type ReminderConfigTableProps = {
    data: ReminderConfig[];
    isLoading: boolean;
    message: string;
};

function ReminderConfigTable({data, isLoading, message}: ReminderConfigTableProps) {
    return (
        <div className="table-wrap">
            <table className="config-table">
                <thead>
                <tr>
                    <th>提醒的开始时间</th>
                    <th>提醒的结束时间</th>
                    <th>配置的开始时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                {data.length > 0 ? (
                    data.map((item) => (
                        <tr key={item.id}>
                            <td>{item.reminderStartTime}</td>
                            <td>{item.reminderEndTime}</td>
                            <td>{item.configStartTime}</td>
                            <td>
                                <div className="action-buttons">
                                    <button type="button">修改</button>
                                    <button className="danger" type="button">
                                        删除
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td className="empty-cell" colSpan={4}>
                            {isLoading ? '正在加载数据...' : message || '暂无数据'}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default ReminderConfigTable;
