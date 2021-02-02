import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";
import React from "react";
import styles from './CrudViewer.module.scss';
import CrudViewerRow from "../CrudViewerRow/CrudViewerRow";
import CrudViewerRowHead from "../CrudViewerRowHead/CrudViewerRowHead";

interface IProps<T> {
    config: ICrudViewerConfiguration;
    entities: T[] | undefined;
    onEdit?: (entity: T) => unknown;
    onDelete?: (entity: T) => unknown;
}

export default function CrudViewer<T>(props: IProps<T>) {
    const {config, entities} = props;
    if (!entities || !config) {
        return <span>Loading...</span>;
    }

    return (
        <div className={styles.table}>
            <CrudViewerRowHead config={config}/>
            {entities.map((entity, index) =>
                <CrudViewerRow
                    config={config}
                    entity={entity}
                    key={index}
                    onEdit={props.onEdit}
                    onDelete={props.onDelete}
                />
            )}
        </div>
    );
}
