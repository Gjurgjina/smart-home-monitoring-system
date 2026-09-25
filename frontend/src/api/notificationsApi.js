const BASE_URL = "http://localhost:8091/api";

function authHeaders() {
    const stored = localStorage.getItem("auth");
    const token = stored ? JSON.parse(stored).token : null;
    return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function fetchAllNotifications() {
    const response = await fetch(`${BASE_URL}/notifications`, {
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch notifications: ${response.status}`);
    }
    return response.json();
}

export async function fetchUnreadNotifications() {
    const response = await fetch(`${BASE_URL}/notifications/unread`, {
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch unread notifications: ${response.status}`);
    }
    return response.json();
}

export async function fetchUnreadCount() {
    const response = await fetch(`${BASE_URL}/notifications/unread-count`, {
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch unread count: ${response.status}`);
    }
    return response.json();
}

export async function markNotificationAsRead(notificationId) {
    const response = await fetch(`${BASE_URL}/notifications/${notificationId}/read`, {
        method: "PATCH",
        headers: authHeaders(),
    });
    if (!response.ok) {
        throw new Error(`Failed to mark notification as read: ${response.status}`);
    }
}