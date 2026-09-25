const BASE_URL = "http://localhost:8091/api";

function authHeaders() {
    const stored = localStorage.getItem("auth");
    const token = stored ? JSON.parse(stored).token : null;
    return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function fetchRecentAlerts() {
    const response = await fetch(`${BASE_URL}/alerts/recent`, {
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch alerts: ${response.status}`);
    }
    return response.json();
}

export async function fetchAlertsBySeverity(severity) {
    const url = severity
        ? `${BASE_URL}/alerts?severity=${severity}`
        : `${BASE_URL}/alerts`;
    const response = await fetch(url, { headers: authHeaders() });
    if (!response.ok) {
        throw new Error(`Failed to fetch alerts: ${response.status}`);
    }
    return response.json();
}

export async function fetchRecentSensorEvents() {
    const response = await fetch(`${BASE_URL}/sensor-events/recent`, {
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch sensor events: ${response.status}`);
    }
    return response.json();
}