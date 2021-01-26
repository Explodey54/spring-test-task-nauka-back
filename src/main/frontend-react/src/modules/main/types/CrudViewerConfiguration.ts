export enum ECrudViewerInputType {
  Text = "TEXT", Select = "SELECT", Color = "COLOR", Date = "DATE", Checkbox = "CHECKBOX"
}

export interface ICrudViewerConfigurationField {
  title: string;
  path: string;
  writePath?: string;
  readOnly?: boolean;
  writeOnly?: boolean;
  isId?: boolean;
  type?: ECrudViewerInputType;
  params?: string;
}

export interface ICrudViewerConfigurationFilterField {
  title: string;
  path: string;
  default?: string;
}

export interface ICrudViewerConfiguration {
  baseApi: string;
  fields: ICrudViewerConfigurationField[];
  path: string;
  filterFields?: ICrudViewerConfigurationFilterField[];
}

export function getIdKey(config: ICrudViewerConfiguration): string {
  const idFields = config.fields.filter(i => i.isId);
  if (idFields.length > 1) {
    console.warn("CrudViewerConfiguration contains more than 1 id field:");
    console.warn(...idFields);
  }
  if (idFields.length === 0) {
    throw new Error("CrudViewerConfiguration doesn't have id field!");
  }
  if (!(idFields[0].writePath || idFields[0].path)) {
    throw new Error("Id field doesn't have path or writePath value!");
  }
  return idFields[0].writePath || idFields[0].path;
}
