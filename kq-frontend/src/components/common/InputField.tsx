interface InputFields {
    label: string,
    value: string,
    onChange: (value: React.SetStateAction<string>) => void,
    type: string,
    placeholder?: string,
    options?: string[]
}

const InputField = ({ label, value, onChange, type, placeholder, options }: InputFields) => {
    return (
        <div className="mb-2">
            <label className="block mb-2">{label}:</label>
            {type === "select" ? (
                <select
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    className="w-full p-2 text-primary-black border-gray-600 rounded-md mb-2 focus:outline-none focus:border-primary-blue focus:ring focus:ring-primary-blue bg-white bg-opacity-90"
                >
                    {placeholder && (
                        <option value="" disabled hidden>
                            {placeholder}
                        </option>
                    )}
                    {options?.map((option, index) => (
                        <option key={index} value={option}>
                            {option}
                        </option>
                    ))}
                </select>
            ) : (
                <input
                    type={type}
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    placeholder={placeholder}
                    className="w-full p-2 text-primary-black border-gray-600 rounded-md mb-2 focus:outline-none focus:border-primary-blue focus:ring focus:ring-primary-blue bg-white bg-opacity-90"
                />
            )}
        </div>
    );
};

export default InputField;
