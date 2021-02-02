import {ICrudViewerConfigurationField} from "../types/CrudViewerConfiguration";

export function isFieldVisibleInRow(field: ICrudViewerConfigurationField): boolean {
    const isBlocked = field.writeOnly;
    return !isBlocked;
}

export function isFieldVisibleInForm(field: ICrudViewerConfigurationField): boolean {
    const isBlocked = field.isId || field.readOnly;
    return !isBlocked;
}
