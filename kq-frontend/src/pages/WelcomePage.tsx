import PlayLogo from '../assets/icons/svg/play.svg';
import DeveloperLogo from '../assets/icons/svg/developer.svg';
import { ButtonWithMx } from '../components/common/Button';
import { UiTexts } from '../consts/uiTexts';
import { Link, useNavigate } from 'react-router-dom';

function WelcomePage() {
    const navigate=useNavigate();

    return (
        <div className="flex flex-col min-w-[375px] h-screen bg-primary-black text-white align-top lg:flex-row overflow-auto">
            <div className="flex flex-col mt-10 items-center lg:items-start lg:justify-center lg:mx-10 lg:w-[40%]">
                <div className="flex mb-2 lg:mx-5">
                </div>
                <div className="flex items-center mt-4 text-center">
                    <h2 className="text-[44px] text-center px-5 md:text-start md:text-title-64">{UiTexts.projectName}</h2>
                </div>
                <div className="flex items-center mt-4">
                    <p className="text-md text-center text-gray-400 mx-5 lg:text-justify">
                        {UiTexts.projectDescription}
                    </p>
                </div>
                <div className="flex flex-col items-center mt-8 mb-6 sm:flex-row lg:mx-5 lg:flex-col xl:flex-row">
                    <div className="mb-4 sm:mb-0 sm:mr-2 lg:mb-4 xl:mb-0">
                        <ButtonWithMx icon={PlayLogo} bgColor="primaryBlue" text="Play" marginX={0} onClick={() => navigate("/puzzle")} isGameStarted={false} />

                    </div>
                    <div className="sm:ml-2 lg:ml-0 xl:ml-2">
                    </div>
                </div>
                <div className="flex items-center  lg:mx-5">
                </div>
            </div>
        </div>
    )
}

export default WelcomePage;