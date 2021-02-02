import React from "react";
import styles from './CrudViewerRow.module.scss';
import {
    ECrudViewerInputType,
    ICrudViewerConfiguration,
    ICrudViewerConfigurationField
} from "../../types/CrudViewerConfiguration";
import {findByPath} from "../../../common/utils/FindByPath";
import cn from 'classnames';
import {isFieldVisibleInRow} from "../../utils/CrudViewer.utils";

interface IProps<T> {
    config: ICrudViewerConfiguration;
    entity: T;
    onEdit?: (entity: T) => unknown;
    onDelete?: (entity: T) => unknown;
}

export default function CrudViewerRow<T> (props: IProps<T>) {
    const handleEditClick = () => {
        if (props.onEdit) props.onEdit(props.entity);
    };
    const handleDeleteClick = () => {
        if (props.onDelete) props.onDelete(props.entity);
    };

    const renderCell = (entity: T, field: ICrudViewerConfigurationField) => {
        const value: string = findByPath(entity, field.path);
        switch (field.type) {
            case ECrudViewerInputType.Color:
                return <div className={styles.colorIndicator} style={{ backgroundColor: value }}/>;
            default:
                return value !== undefined ? String(value) : '';
        }
    }

    const cellList = props.config.fields
        .filter(isFieldVisibleInRow)
        .map(field =>
            <div
                className={cn(styles.cell, {[styles.cellId]: field.isId})}
                key={field.path}
            >
                {renderCell(props.entity, field)}
            </div>
        );

    return (
        <div className={styles.row}>
            { cellList }
            <div className={styles.cell}>
                <button onClick={handleEditClick}>Edit</button>
                <button onClick={handleDeleteClick}>Delete</button>
            </div>
        </div>
    );
}
