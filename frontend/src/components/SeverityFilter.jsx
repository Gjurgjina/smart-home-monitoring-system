const OPTIONS = [
    { label: "All", value: null },
    { label: "Critical", value: "CRITICAL" },
    { label: "Warning", value: "WARNING" },
];

export default function SeverityFilter({ selected, onChange }) {
    return (
        <div className="severity-filter">
            {OPTIONS.map((option) => (
                <button
                    key={option.label}
                    className={selected === option.value ? "active" : ""}
                    onClick={() => onChange(option.value)}
                >
                    {option.label}
                </button>
            ))}
        </div>
    );
}