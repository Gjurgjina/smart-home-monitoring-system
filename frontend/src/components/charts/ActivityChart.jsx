const BUCKET_COUNT = 8;
const BUCKET_MINUTES = 15;

function buildBuckets(alerts) {
    const now = Date.now();
    const buckets = Array.from({ length: BUCKET_COUNT }, (_, i) => ({
        critical: 0,
        warning: 0,
        label: `${(BUCKET_COUNT - i - 1) * BUCKET_MINUTES}m`,
    }));

    alerts.forEach((alert) => {
        const ts = new Date(alert.timestamp).getTime();
        const minutesAgo = (now - ts) / 60000;
        const bucketIndex = BUCKET_COUNT - 1 - Math.floor(minutesAgo / BUCKET_MINUTES);
        if (bucketIndex >= 0 && bucketIndex < BUCKET_COUNT) {
            if (alert.severity === "CRITICAL") buckets[bucketIndex].critical += 1;
            else buckets[bucketIndex].warning += 1;
        }
    });

    return buckets;
}

export default function ActivityChart({ alerts }) {
    const buckets = buildBuckets(alerts);
    const max = Math.max(1, ...buckets.map((b) => b.critical + b.warning));

    return (
        <div className="activity-chart">
            <div className="activity-bars">
                {buckets.map((b, i) => {
                    const total = b.critical + b.warning;
                    const heightPct = (total / max) * 100;
                    return (
                        <div className="activity-bar-col" key={i}>
                            <div
                                className="activity-bar"
                                style={{ height: `${total > 0 ? Math.max(heightPct, 6) : 2}%` }}
                            >
                                {b.critical > 0 && (
                                    <div
                                        className="activity-bar-segment critical"
                                        style={{ height: `${(b.critical / total) * 100}%` }}
                                    />
                                )}
                                {b.warning > 0 && (
                                    <div
                                        className="activity-bar-segment warning"
                                        style={{ height: `${(b.warning / total) * 100}%` }}
                                    />
                                )}
                            </div>
                            <span className="activity-bar-label">{b.label}</span>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}