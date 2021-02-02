import CrudViewer from "../components/CrudViewer/CrudViewer";
import CrudForm from "../components/CrudForm/CrudForm";
import React, {useEffect, useState} from "react";
import CrudFetch from "../../common/services/CrudFetch";
import {ICrudViewerConfiguration} from "../types/CrudViewerConfiguration";
import Identifiable from "../types/Identifiable";

enum EFormAction {
    CREATE, EDIT
}

interface IProps {
    config: ICrudViewerConfiguration;
}

export default function CrudContainer<T extends Identifiable>({ config }: IProps) {
    const [entityList, setEntityList] = useState<T[]>();
    const [selectedEntity, setSelectedEntity] = useState<T>();
    const [formAction, setFormAction] = useState<EFormAction>();
    const [isFormVisible, setIsFormVisible] = useState<boolean>(false);

    useEffect(() => {
        getAll();
    }, [config]);

    const handleDelete = (entity: T) => {
        CrudFetch.remove(config.baseApi, entity.id)
            .then(() => getAll());
    }

    const handleEdit = (entity: T) => {
        setSelectedEntity(entity);
        setIsFormVisible(true);
        setFormAction(EFormAction.EDIT);
    }

    const handleCreate = () => {
        setSelectedEntity(undefined);
        setIsFormVisible(true);
        setFormAction(EFormAction.CREATE);
    }

    const handleFormSubmit = (form: Partial<T>) => {
        let request;
        switch (formAction) {
            case EFormAction.EDIT:
                if (!selectedEntity) break;
                request = CrudFetch.edit(config.baseApi, selectedEntity.id, form);
                break;
            case EFormAction.CREATE:
                request = CrudFetch.create(config.baseApi, form);
                break;
        }
        if (!request) return;
        request.then(i => {
            if (!i.ok) {
                return;
            }
            setIsFormVisible(false);
            getAll();
        });
    }

    const getAll = (callback?: () => unknown): void => {
        CrudFetch.getAll(config.baseApi).then(i => {
            if (!i.ok) return;
            setEntityList(i.data);
            if (callback) callback();
        })
    }

    return (
        <div>
            <button onClick={handleCreate}>Create</button>
            <CrudViewer
                config={config}
                entities={entityList}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />
            {isFormVisible &&
                <CrudForm
                    config={config}
                    entity={selectedEntity}
                    onSubmit={handleFormSubmit}
                />
            }
        </div>
    )
}
