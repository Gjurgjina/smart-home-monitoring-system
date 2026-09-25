export default function SeverityDonut({ critical, warning }) {
    const total = critical + warning;
    const criticalPct = total === 0 ? 0 : (critical / total) * 100;

    const gradient =
        total === 0
            ? "conic-gradient(#333a46 0% 100%)"
            : `conic-gradient(#ef4444 0% ${criticalPct}%, #f59e0b ${criticalPct}% 100%)`;

    return (
        <div className="donut-wrap">
            <div className="donut-ring" style={{ background: gradient }}>
                <div className="donut-center">
                    <span className="donut-center-value">{total}</span>
                    <span className="donut-center-label">alerts</span>
                </div>
            </div>
            <div className="donut-legend">
        <span className="legend-item">
          <span className="legend-dot critical" />
          Critical <strong>{critical}</strong>
        </span>
                <span className="legend-item">
          <span className="legend-dot warning" />
          Warning <strong>{warning}</strong>
        </span>
            </div>
        </div>
    );
}