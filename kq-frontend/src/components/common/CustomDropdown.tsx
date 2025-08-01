import { useState, useRef, useEffect } from "react";
import { EnumType } from "typescript";
import up_arrow from '../../assets/icons/svg/up_arrow.svg';
import down_arrow from '../../assets/icons/svg/down_arrow.svg';

export interface CustomDropDown {
    title: string;
    defaultOption: string;
    options: EnumType;
    onChange: (selectedItem: EnumType) => void;
    disabled?: boolean;
}

export default function CustomDropDown({ title, defaultOption, options, onChange, disabled }: CustomDropDown) {
    const [isOpen, setIsOpen] = useState<boolean>(false);
    const [position, setPosition] = useState<"down" | "up">("down");
    const dropdownRef = useRef<HTMLDivElement>(null);

    const keys = Object.keys(options);

    const handleToggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    useEffect(() => {
        if (isOpen && dropdownRef.current) {
            const dropdownRect = dropdownRef.current.getBoundingClientRect();
            const windowHeight = window.innerHeight;

            // Check if there is enough space below the button
            if (windowHeight - dropdownRect.bottom < dropdownRect.height) {
                setPosition("up");
            } else {
                setPosition("down");
            }
        }
    }, [isOpen]);

    return (
        <div className="relative w-auto mb-4">
            <div className="flex justify-between items-center pb-[2px] text-title-16">
                <p>{title}</p>
                <button
                    className={`flex justify-center items-center rounded-md px-4 py-2 ml-2 ${disabled ? 'bg-primary-border cursor-not-allowed' : 'bg-primary-border'} transition-all hover:bg-slate-700`}
                    aria-expanded="true"
                    aria-haspopup="true"
                    onClick={handleToggleDropdown}
                    disabled={disabled}
                >
                    {defaultOption} <img src={isOpen ? up_arrow : down_arrow} alt="open/close" className="ml-2 lg:ml-8" />
                </button>
            </div>

            {isOpen && (
                <div
                    ref={dropdownRef}
                    className={`absolute ${position === "up" ? "bottom-full" : "top-full"} bg-primary-black right-0 z-50 border-[2px] border-primary-black w-[50%] rounded-md`}
                >
                    {keys.map((key, index) => (
                        <ChangeOptionButton
                            key={index}
                            index={index}
                            options={options}
                            onChange={(newOrder) => {
                                onChange(newOrder);
                                setIsOpen(false);
                            }}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}

interface ChangeOption {
    index: number;
    onChange: (order: EnumType) => void;
    options: EnumType;
}

function ChangeOptionButton({ index, options, onChange }: ChangeOption) {
    return (
        <div 
            className="py-1 bg-primary-grey text-center hover:cursor-pointer border-t-[2px] border-primary-border w-full rounded-md transition-all hover:bg-primary-blue hover:border-primary-blue"
            onClick={() => {
                onChange(Object.entries(options)[index][1]);
            }}
        >
            {Object.entries(options)[index][1]}
        </div>
    );
}
