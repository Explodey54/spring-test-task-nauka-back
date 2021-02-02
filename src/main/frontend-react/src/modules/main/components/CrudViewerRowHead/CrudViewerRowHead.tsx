import React from "react";
import {ICrudViewerConfiguration} from "../../types/CrudViewerConfiguration";
import styles from './CrudViewerRowHead.module.scss';
import cn from "classnames";
import {isFieldVisibleInRow} from "../../utils/CrudViewer.utils";

interface IProps<T> {
    config: ICrudViewerConfiguration;
}

export default function CrudViewerRowHead<T> (props: IProps<T>) {
    const cellList = props.config.fields
        .filter(isFieldVisibleInRow)
        .map(field =>
            <div
                className={cn(styles.cell, {[styles.cellId]: field.isId})}
                key={field.path}
                >{field.title}
            </div>
        );
    return (
        <div className={styles.row}>
            { cellList }
        </div>
    );
}
