import { useState, useEffect, useCallback } from "react";


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
        load();

        const intervalId = setInterval(load, intervalMs);

        return () => clearInterval(intervalId);
    }, [load, intervalMs]);

    return { data, error, loading, refetch: load };
}