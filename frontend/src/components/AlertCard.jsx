function LocationIcon() {
    return (
        <svg
            className="location-icon"
            viewBox="0 0 512 512"
            width="12"
            height="12"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
        >
            <path
                d="M256 16C158.8 16 80 94.8 80 192c0 111 141 289 169 322.7a10.2 10.2 0 0 0 14 0C291 481 432 303 432 192 432 94.8 353.2 16 256 16z"
                stroke="currentColor"
                strokeWidth="32"
                strokeLinejoin="round"
            />
            <circle
                cx="256"
                cy="192"
                r="80"
                stroke="currentColor"
                strokeWidth="32"
            />
        </svg>
    );
}

export default function AlertCard({ alert }) {
    const isCritical = alert.severity === "CRITICAL";

    return (
        <div className={`alert-card ${isCritical ? "critical" : "warning"}`}>
            <div className="alert-header">
                <span className="alert-severity">{alert.severity}</span>
                <span className="alert-device-type">{alert.deviceType}</span>
            </div>
            <p className="alert-message">{alert.message}</p>
            <div className="alert-footer">
        <span className="alert-room">
         <LocationIcon />
            {formatRoomName(alert.roomId)}
        </span>
                <span className="alert-time">
          {new Date(alert.timestamp).toLocaleTimeString()}
        </span>
            </div>
        </div>
    );
}

function formatRoomName(roomId) {
    return roomId
        .split("-")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join("-");
}