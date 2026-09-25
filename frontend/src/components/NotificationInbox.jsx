import { useState } from "react";
import { markNotificationAsRead } from "../api/notificationsApi";

export default function NotificationInbox({ notifications, unreadCount, hasNew, justArrived, clearNew, onRead }) {
    const [open, setOpen] = useState(false);

    function toggleOpen() {
        setOpen((v) => {
            const next = !v;
            if (next) clearNew();
            return next;
        });
    }

    async function handleClick(notification) {
        if (!notification.read) {
            await markNotificationAsRead(notification.notificationId);
            onRead();
        }
    }

    return (
        <div className="inbox">
            <button
                className={`inbox-trigger ${hasNew ? "has-new" : ""} ${justArrived ? "flash" : ""}`}
                onClick={toggleOpen}
            >
                Inbox
                {unreadCount > 0 && <span className="inbox-badge">{unreadCount}</span>}
            </button>

            {open && (
                <div className="inbox-panel">
                    {notifications.length === 0 && (
                        <p className="inbox-empty">No notifications yet.</p>
                    )}
                    {notifications.map((n) => (
                        <div
                            key={n.notificationId}
                            className={`inbox-item ${n.read ? "read" : "unread"} ${n.severity?.toLowerCase() || ""}`}
                            onClick={() => handleClick(n)}
                        >
                            <div className="inbox-item-top">
                                <span className={`inbox-severity ${n.severity.toLowerCase()}`}>
                                    {n.severity}
                                </span>
                                <span className="inbox-time">
                                    {new Date(n.timestamp).toLocaleString()}
                                </span>
                            </div>
                            <p className="inbox-message">{n.message}</p>
                            <span className="inbox-location">
                                Apartment {n.apartmentId?.replace("apt-", "")} · {n.roomId}
                            </span>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}