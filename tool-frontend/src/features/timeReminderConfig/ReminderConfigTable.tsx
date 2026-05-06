import type {ReminderConfig} from '../../types/reminderConfig';

type ReminderConfigTableProps = {
    data: ReminderConfig[];
    isLoading: boolean;
    message: string;
    deletingId: string | number | null;
    onEdit: (item: ReminderConfig) => void;
    onDelete: (id: string | number) => void;
};

function ReminderConfigTable({
                                 data,
                                 isLoading,
                                 message,
                                 deletingId,
                                 onEdit,
                                 onDelete,
                             }: ReminderConfigTableProps) {
    return (
        <div className="table-wrap">
            <table className="config-table">
                <thead>
                <tr>
                    <th>Reminder start</th>
                    <th>Reminder end</th>
                    <th>Config time</th>
                    <th>Actions</th>
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
                                    <button type="button" onClick={() => onEdit(item)}>
                                        Edit
                                    </button>
                                    <button
                                        className="danger"
                                        type="button"
                                        onClick={() => onDelete(item.id)}
                                        disabled={deletingId === item.id}
                                    >
                                        {deletingId === item.id ? 'Deleting' : 'Delete'}
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td className="empty-cell" colSpan={4}>
                            {isLoading ? 'Loading data...' : message || 'No data'}
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default ReminderConfigTable;
