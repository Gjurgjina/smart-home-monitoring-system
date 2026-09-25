import { useState, useEffect, useCallback } from "react";

/**
 * Custom hook koj periodichno (na sekoi intervalMs) povtorno povikuva
 * fetchFunction i go vrakja rezultatot. Se koristi za "live" azhuriranje
 * na podatocite bez websocket - obichen polling.
 */
export function usePolling(fetchFunction, intervalMs = 3000) {
    const [data, setData] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    const load = useCallback(async () => {
        try {
            const result = await fetchFunction();
            setData(result);
            setError(null);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, [fetchFunction]);

    useEffect(() => {
        load(); // prv povik veднаш, ne chekaj go prviot interval

        const intervalId = setInterval(load, intervalMs);

        return () => clearInterval(intervalId); // cistenje koga komponentata se demontira
    }, [load, intervalMs]);

    return { data, error, loading, refetch: load };
}