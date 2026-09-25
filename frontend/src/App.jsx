import { useState, useMemo, useCallback } from "react";
import { useAuth } from "./context/AuthContext";
import { usePolling } from "./hooks/usePolling";
import { useNotificationPolling } from "./hooks/useNotificationPolling";
import { fetchAlertsBySeverity } from "./api/alertsApi";
import LoginPage from "./components/LoginPage";
import AlertList from "./components/AlertList";
import SeverityFilter from "./components/SeverityFilter";
import ConnectionStatus from "./components/ConnectionStatus";
import NotificationInbox from "./components/NotificationInbox";
import { AlertOctagonIcon, AlertTriangleIcon, ListIcon, BellIcon } from "./components/icons/Icons";
import SeverityDonut from "./components/charts/SeverityDonut";
import ActivityChart from "./components/charts/ActivityChart";
import "./App.css";

function Dashboard() {
    const { user, logout } = useAuth();
    const [severity, setSeverity] = useState(null);

    const fetchFn = useCallback(() => fetchAlertsBySeverity(severity), [severity]);
    const { data: alerts, error, loading } = usePolling(fetchFn, 3000);

    const {
        unreadCount,
        notifications,
        hasNew,
        justArrived,
        clearNew,
        refresh: refreshNotifications,
    } = useNotificationPolling(4000);

    const criticalCount = useMemo(
        () => alerts.filter((a) => a.severity === "CRITICAL").length,
        [alerts]
    );
    const warningCount = useMemo(
        () => alerts.filter((a) => a.severity === "WARNING").length,
        [alerts]
    );

    return (
        <div className="page">
            <div className="app">
                <header className="app-header">
                    <div className="header-text">
            <span className="eyebrow">
              Monitoring {user.role === "ADMIN" ? "· All apartments" : `· Apartment ${user.apartmentId?.replace("apt-", "")}`}
            </span>
                        <h1>Home Sensors</h1>
                    </div>
                    <div className="header-actions">
                        <ConnectionStatus error={error} />
                        <NotificationInbox
                            notifications={notifications}
                            unreadCount={unreadCount}
                            hasNew={hasNew}
                            justArrived={justArrived}
                            clearNew={clearNew}
                            onRead={refreshNotifications}
                        />
                        <button className="logout-button" onClick={logout}>
                            Sign out
                        </button>
                    </div>
                </header>

                <div className="stats-grid">
                    <div className="stat-card info">
                        <div className="stat-icon-wrap">
                            <ListIcon />
                        </div>
                        <div>
                            <span className="stat-value">{alerts.length}</span>
                            <span className="stat-label">Total alerts</span>
                        </div>
                    </div>
                    <div className="stat-card critical">
                        <div className="stat-icon-wrap">
                            <AlertOctagonIcon />
                        </div>
                        <div>
                            <span className="stat-value">{criticalCount}</span>
                            <span className="stat-label">Critical</span>
                        </div>
                    </div>
                    <div className="stat-card warning">
                        <div className="stat-icon-wrap">
                            <AlertTriangleIcon />
                        </div>
                        <div>
                            <span className="stat-value">{warningCount}</span>
                            <span className="stat-label">Warning</span>
                        </div>
                    </div>
                    <div className="stat-card info">
                        <div className="stat-icon-wrap">
                            <BellIcon />
                        </div>
                        <div>
                            <span className="stat-value">{unreadCount}</span>
                            <span className="stat-label">Unread</span>
                        </div>
                    </div>
                </div>

                <div className="charts-row">
                    <div className="chart-card">
                        <p className="chart-card-title">Severity breakdown</p>
                        <SeverityDonut critical={criticalCount} warning={warningCount} />
                    </div>
                    <div className="chart-card">
                        <p className="chart-card-title">Activity · last 2h</p>
                        <ActivityChart alerts={alerts} />
                    </div>
                </div>

                <SeverityFilter selected={severity} onChange={setSeverity} />

                <AlertList alerts={alerts} loading={loading} error={error} />
            </div>

        </div>
    );
}

function App() {
    const { user } = useAuth();
    return user ? <Dashboard /> : <LoginPage />;
}

export default App;