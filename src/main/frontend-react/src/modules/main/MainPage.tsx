import React from 'react';
import {History} from 'history';
import Auth from "../common/services/Auth";
import CrudRoutes from "./CrudRoutes";
import {ICrudViewerConfiguration} from "./types/CrudViewerConfiguration";
import CrudViewer from "./components/CrudViewer/CrudViewer";
import CrudForm from "./components/CrudForm/CrudForm";
import CrudFetch from "../common/services/CrudFetch";
import Identifiable from "./types/Identifiable";
import CrudLinkList from "./components/CrudLinkList/CrudLinkList";
import {Route, Switch } from 'react-router-dom';
import CrudContainer from "./containers/CrudContainer";



interface IProps {
    history: History;
}
//
// interface IState<T> {
//     entityList?: T[];
//     selectedEntity?: T;
//     formAction?: EFormAction;
//     isFormVisible: boolean;
// }

class MainPage<T extends Identifiable> extends React.Component<IProps> {
    // constructor(props: IProps) {
    //     super(props);
    // }

    logout = (): void => {
        Auth.logout();
    }
    getCurrentCrudRoute = (): ICrudViewerConfiguration | undefined => {
        const path = this.props.history.location.pathname;
        let found = CrudRoutes.find(i => path.endsWith(i.path));
        if (!found) {
            found = CrudRoutes.find(Boolean);
            if (!found) {
                throw new Error("Couldn't get crud route!");
            }
            this.props.history.push(found.path);
            return;
        }

        return found;
    }

    render() {
        const routes = CrudRoutes.map(i =>
            <Route path={'/' + i.baseApi} key={i.baseApi} render={() => {
                const config = this.getCurrentCrudRoute();
                return config ? <CrudContainer config={config}/> : null;
            }}/>
        );
        return (
            <div>
                <CrudLinkList/>
                <Switch>
                    {routes}
                </Switch>

                <button onClick={this.logout}>Log Out</button>
            </div>
        );
    }

}

export default MainPage;
