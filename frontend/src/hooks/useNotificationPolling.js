import { useState, useEffect, useRef, useCallback } from "react";
import { fetchUnreadCount, fetchAllNotifications } from "../api/notificationsApi";

export function useNotificationPolling(intervalMs = 4000) {
    const [unreadCount, setUnreadCount] = useState(0);
    const [notifications, setNotifications] = useState([]);
    const [hasNew, setHasNew] = useState(false);
    const [justArrived, setJustArrived] = useState(false);
    const previousUnreadRef = useRef(0);
    const isFirstLoadRef = useRef(true);
    const flashTimeoutRef = useRef(null);

    const refresh = useCallback(async () => {
        try {
            const [countData, allNotifications] = await Promise.all([
                fetchUnreadCount(),
                fetchAllNotifications(),
            ]);
            const newCount = countData.count;

            if (isFirstLoadRef.current) {
                isFirstLoadRef.current = false;
            } else if (newCount > previousUnreadRef.current) {
                setHasNew(true);
                setJustArrived(true);
                if (flashTimeoutRef.current) clearTimeout(flashTimeoutRef.current);
                flashTimeoutRef.current = setTimeout(() => setJustArrived(false), 3000);
            }

            previousUnreadRef.current = newCount;
            setUnreadCount(newCount);
            setNotifications(allNotifications);
        } catch {

        }
    }, []);

    useEffect(() => {
        refresh();
        const intervalId = setInterval(refresh, intervalMs);
        return () => {
            clearInterval(intervalId);
            if (flashTimeoutRef.current) clearTimeout(flashTimeoutRef.current);
        };
    }, [refresh, intervalMs]);

    const clearNew = useCallback(() => {
        setHasNew(false);
        setJustArrived(false);
        if (flashTimeoutRef.current) clearTimeout(flashTimeoutRef.current);
    }, []);

    return { unreadCount, notifications, hasNew, justArrived, clearNew, refresh };
}