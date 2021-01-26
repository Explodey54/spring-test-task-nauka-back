import React from 'react';
import './App.css';
import { BrowserRouter as Router, Switch } from 'react-router-dom';
import PrivateRoute from "./modules/common/components/PrivateRoute";
import AuthPage from "./modules/auth/pages/AuthPage/AuthPage";
import MainPage from "./modules/main/MainPage";
import Auth from "./modules/common/services/Auth";

function App() {
    return (
        <Router>
            <Switch>
                <PrivateRoute
                    path="/auth"
                    component={AuthPage}
                    canAccess={() => !Auth.isLoggedIn()}
                    redirect='/'
                />
                <PrivateRoute
                    path="/*"
                    component={MainPage}
                    canAccess={() => Auth.isLoggedIn()}
                    redirect='/auth'
                />
            </Switch>
        </Router>
    );
}

export default App;
