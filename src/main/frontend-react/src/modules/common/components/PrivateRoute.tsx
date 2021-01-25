import * as React from "react";
import { Route, Redirect, RouteProps } from 'react-router-dom';

interface IProps extends RouteProps {
    component: React.ComponentType<any>,
    canAccess: boolean | (() => boolean),
    redirect: string
}

function PrivateRoute({ component: Component, redirect, canAccess, ...rest }: IProps) {
    const access = typeof canAccess === "function" ? canAccess() : canAccess;
    return <Route {...rest} render={(props) => (
        access
            ? <Component {...props} />
            : <Redirect to={redirect}/>
    )}/>
}

export default PrivateRoute;
