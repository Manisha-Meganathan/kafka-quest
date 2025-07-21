import { HowToPlayInstruction, tutorials } from "../../../consts/tutorialData";

export default function HowToPlay() {
    return (
        <>
            {tutorials.map((tutorial) => (
                <Tutorial key={tutorial.title} title={tutorial.title} instructions={tutorial.instructions} />
            ))}
        </>
    );
}

function Tutorial({ title, instructions }: HowToPlayInstruction) {
    return (
        <>
            <div className="w-full border-[1px] border-default-border-color rounded-md bg-primary-black py-3 px-2 my-3">
                <div className="flex flex-row text-title-16 mb-2 items-top">
                    <div>
                        <div className="w-3 h-3 border-none bg-primary-blue rounded-[50%] inline-block mr-1 mt-1.5"></div>
                    </div>
                    <div className="ml-1">{title}</div>
                </div>
                <div className=" ml-4">
                    {instructions.map(instruction => (
                        <div key={instruction.number}>
                            <p className="inline text-sm text-white text-opacity-70 text-justify"> {instruction.number + "."} {instruction.instruction} </p>
                            <hr className=" border-primary-border border-[1px] my-2" />
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}