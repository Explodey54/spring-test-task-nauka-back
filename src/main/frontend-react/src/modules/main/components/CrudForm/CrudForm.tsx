import {
    ECrudViewerInputType,
    ICrudViewerConfiguration,
    ICrudViewerConfigurationField
} from "../../types/CrudViewerConfiguration";
import {useForm} from "react-hook-form";
import React, {useEffect, useMemo} from "react";
import Autocomplete from "../../../common/components/Autocomplete/Autocomplete";
import {isFieldVisibleInForm} from "../../utils/CrudViewer.utils";
import {Dictionary} from "../../../common/types/Dictionary";
import {findByPath} from "../../../common/utils/FindByPath";

interface IProps<T> {
    config: ICrudViewerConfiguration;
    entity?: any;
    onSubmit?: (value: Partial<T>) => unknown
}

function createDefaultValues<T = any>(config: ICrudViewerConfiguration, entity: T): Dictionary {
    const output: Dictionary = {};
    config.fields
        .filter(isFieldVisibleInForm)
        .forEach(i => {
            const path = i.writePath || i.path;
            output[path] = findByPath(entity, i.path);
        })
    return output;
}

export default function CrudForm<T>(props: IProps<T>) {
    const defaultValues: any = useMemo(() => createDefaultValues(props.config, props.entity), [props.entity]);
    const { register, handleSubmit, reset, setValue } = useForm();
    const updateForm = () => {
        Object.entries(defaultValues).forEach(([key, value]) => {
            setValue(key, value);
        });
    }

    // On props.entity change - update form
    useEffect(() => updateForm(), [props.entity]);

    const onSubmit = (data: any) => {
        if (props.onSubmit) props.onSubmit(data);
    }

    const renderInput = (field: ICrudViewerConfigurationField, ref: any): JSX.Element => {
        const path = field.writePath || field.path;
        let input: JSX.Element;
        switch (field.type) {
            case ECrudViewerInputType.Text:
                input = <input name={path} type='text' ref={ref}/>;
                break;
            case ECrudViewerInputType.Autocomplete:
                input = <Autocomplete url={field.params} name={path} ref={ref} onFetch={updateForm}/>;
                break;
            case ECrudViewerInputType.Date:
                input = <input name={path} type='date' ref={ref}/>;
                break;
            case ECrudViewerInputType.Checkbox:
                input = <input name={path} type='checkbox' ref={ref}/>;
                break;
            case ECrudViewerInputType.Color:
                input = <input name={path} type='color' ref={ref}/>;
                break;
            default:
                input = <input name={path} type='text' ref={ref}/>;
        }
        return (
            <label key={path}>
                <span>{field.title}</span>
                {input}
            </label>
        );
    }

    const fields = props.config.fields
        .filter(isFieldVisibleInForm)
        .map(i => renderInput(i, register));

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            {fields}
            <input type="submit" value='Submit'/>
        </form>
    );
}
