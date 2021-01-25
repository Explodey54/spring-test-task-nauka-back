import React from 'react';
import { useHistory } from 'react-router-dom';
import Auth from "../common/services/Auth";

function MainPage() {
    const history = useHistory();

    const logout = () => {
        Auth.logout();
        history.push('/auth');
    };
    return <button onClick={logout}>Log Out</button>;
}

export default MainPage;
