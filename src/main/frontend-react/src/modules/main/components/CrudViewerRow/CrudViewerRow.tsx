import React from "react";
import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";
import {findByPath} from "../../../common/utils/FindByPath";

interface IProps<T> {
    config: ICrudViewerConfiguration;
    entity: T;
}

export default function CrudViewerRow<T> (props: IProps<T>) {
    return (
        <React.Fragment>
            {props.config.fields.map(field =>
                <div key={field.path}>{findByPath(props.entity, field.path)}</div>
            )}
        </React.Fragment>
    );
}
