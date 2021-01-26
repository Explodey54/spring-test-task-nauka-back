import React from 'react';
import { useHistory } from "react-router-dom";
import AuthForm, {IAuthForm} from "../../components/AuthForm/AuthForm";
import Auth from "../../../common/services/Auth";

function AuthPage() {
    const history = useHistory();
    const [error, setError] = React.useState<string>();

    const handleSubmit = async (value: IAuthForm) => {
        setError(undefined);
        const resp = await Auth.login(value);
        const { ok, reason } = resp;
        if (!ok) {
            setError(reason);
            return;
        }
        history.push('/');
    }

    return (
        <React.Fragment>
            <AuthForm onSubmit={handleSubmit}/>
            {error &&
                <span>{error}</span>
            }
        </React.Fragment>

    );
}

export default AuthPage;
