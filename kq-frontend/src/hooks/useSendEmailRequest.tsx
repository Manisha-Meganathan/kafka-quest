import { useState } from 'react';
import axios from 'axios';
import { sendEmailRequestEndPoint } from '../consts/endpoints';
import { CommonTypes } from '../types/common';
import { UiTexts } from '../consts/uiTexts';

const useSendEmailRequest = () => {
    const [loading, setLoading] = useState(false);
    const [status, setStatus] = useState('');

    const sendEmail = async (formData : CommonTypes.DemoRequest) => {
        const url = sendEmailRequestEndPoint;
        setLoading(true);
        try {
            await axios.post(url, formData);
            setStatus(UiTexts.emailSendSuccess);
        } catch (error) {
            console.error(error);
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 500) {
                    console.log(error.message);
                    setStatus(UiTexts.emailSendFailure);
                } else {
                    console.log(error.message);
                    setStatus(UiTexts.emailSendFailure);
                }
            }
        } finally {
            setLoading(false);
        }
    };
    return { loading, status, sendEmail };
}

export default useSendEmailRequest;
