import React from 'react';
import {BrowserRouter as Router, Switch, Route, Redirect} from 'react-router-dom';
import {History} from 'history';
import Auth from "../common/services/Auth";
import {crudRoutes} from "./crud-routes";
import BaseFetch from "../common/services/BaseFetch";
import {ICrudViewerConfiguration} from "./types/CrudViewerConfiguration";
import CrudViewer from "./components/CrudViewer/CrudViewer";

interface IProps {
    history: History;
}

interface IState<T> {
    entityList: T[] | null;
}

class MainPage<T> extends React.Component<IProps, IState<T>> {
    private readonly currentRoute?: ICrudViewerConfiguration;

    constructor(props: IProps) {
        super(props);
        const { history } = props;
        this.state = {
            entityList: null
        };

        // check if current route is found from url
        // if not found get and push first one from list
        this.currentRoute = this.getCurrentCrudRoute();
        if (!this.currentRoute) {
            const firstCrudRoute = crudRoutes.find(Boolean);
            if (firstCrudRoute) {
                history.push(firstCrudRoute.path);
            }
        }
    }

    componentDidMount(): void {
        if (!this.currentRoute) return;
        this.fetchAll(this.currentRoute);
    }

    logout = (): void => {
        Auth.logout();
        this.props.history.push('/auth');
    }

    private fetchAll(route: ICrudViewerConfiguration): void {
        BaseFetch.fetchJson(route.baseApi).then(i => {
            if (!i.ok) return;
            this.setState({entityList: i.data});
        })
    }

    private getCurrentCrudRoute(): ICrudViewerConfiguration | undefined {
        const path = this.props.history.location.pathname;
        return crudRoutes.find(i => path.endsWith(i.path));
    }

    render() {
        const firstRoute = crudRoutes.find(Boolean);
        const routes = crudRoutes.map(i =>
            <Route path={`/${i.baseApi}`} key={i.baseApi}>
                <CrudViewer config={i} entities={this.state.entityList}/>
            </Route>
        );
        return (
            <Router>
                <Switch>
                    {routes}
                    <Redirect to={firstRoute?.baseApi || '/'}/>
                </Switch>
                <button onClick={this.logout}>Log Out</button>
            </Router>
        );
    }

}

export default MainPage;
