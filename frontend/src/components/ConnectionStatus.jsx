export default function ConnectionStatus({ error }) {
    if (error) {
        return (
            <div className="connection-status offline">
                <span className="status-dot" />
                Connection lost
            </div>
        );
    }

    return (
        <div className="connection-status online">
            <span className="status-dot" />
            Live
        </div>
    );
}