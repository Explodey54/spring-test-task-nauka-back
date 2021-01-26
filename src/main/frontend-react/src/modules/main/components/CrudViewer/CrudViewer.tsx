import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";
import React from "react";
import CrudViewerRow from "../CrudViewerRow/CrudViewerRow";

interface IProps<T> {
    config: ICrudViewerConfiguration;
    entities: T[] | null;
}

export default function CrudViewer<T>(props: IProps<T>) {
    const {config, entities} = props;
    if (!entities || !config) {
        return <span>Loading...</span>;
    }
    return (
        <React.Fragment>
            {entities.map((entity, index) =>
                <CrudViewerRow config={config} entity={entity} key={index}/>
            )}
        </React.Fragment>
    );
}
