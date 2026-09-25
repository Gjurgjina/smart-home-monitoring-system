import AlertCard from "./AlertCard";

export default function AlertList({ alerts, loading, error }) {
    if (loading) {
        return <p className="status-message">Loading...</p>;
    }

    if (error) {
        return <p className="status-message error">Error: {error}</p>;
    }

    if (alerts.length === 0) {
        return <p className="status-message">No alerts right now.</p>;
    }

    return (
        <div className="alert-list">
            {alerts.map((alert) => (
                <AlertCard key={alert.alertId} alert={alert} />
            ))}
        </div>
    );
}