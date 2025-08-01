export interface instruction {
    number: number
    instruction: string
}
export interface HowToPlayInstruction {
    title: string,
    instructions: instruction[]
}
export const tutorials: HowToPlayInstruction[] = [
    {
        title: "Start to play:",
        instructions: [
            {
                number: 1,
                instruction: "Press start, and it will prompt a username to identify the player. Enter your username and hit " +
                    "login if you are already registered; otherwise, hit sign up."
            },
            {
                number: 2,
                instruction: "wait for the game to start."
            },
            {
                number: 3,
                instruction: "In the right panel, you can now see four random draggable image pieces at a time."
            },
            {
                number: 4,
                instruction: "Drag and drop them to any of the left grid's positions."
            }
        ]
    }, {
        title: "Reshuffle:",
        instructions: [
            {
                number: 1,
                instruction: "You can always reshuffle when you want to get a different set of random " +
                    "pieces, or all the pieces from the panel are dragged and dropped to the grid."
            }
        ]
    }, {
        title: "Remove an added piece from the grid:",
        instructions: [
            {
                number: 1,
                instruction: "If you dropped a piece in the incorrect grid position, you can remove " +
                    "it from the grid by clicking over it. The removed piece, will be available to reshuffle."
            }
        ]
    }, {
        title: "Revert moves up to a point:",
        instructions: [
            {
                number: 1,
                instruction: "You can revert a set of events at once by clicking on any of the events" +
                    " in the event panel. By doing this, all the latest moves from the selected event will" +
                    " be reverted (the selected event will be excluded)."
            }
        ]
    }, {
        title: "Reset game:",
        instructions: [
            {
                number: 1,
                instruction: "If you want to completely reset the game and start from the beginning," +
                    " you can press the reset button from the bottom button panel. It'll prompt to a " +
                    "confirmation, which you can select to reset the game."
            }
        ]
    }, {
        title: "Completing the game:",
        instructions: [
            {
                number: 1,
                instruction: "Dragging and dropping all the pieces to the grid will not end the game; " +
                    "instead, each piece should be added to the correct position in order to end the game."
            }
        ]
    }, {
        title: "Score:",
        instructions: [
            {
                number: 1,
                instruction: "You'll receive a score card with a grade (bad, good, or excellent) and the" +
                    " elapsed time once you complete the game."
            }
        ]
    }
]