enum StorageType {
    LocalStorage,
    SessionStorage
}

const STORAGE_PREFIX = 'nauka';

const createStorageKey = (key: string) => `${STORAGE_PREFIX}-${key}`;
const getStorageFromType = (type: StorageType): Storage => {
    switch (type) {
        case StorageType.LocalStorage:
            return window.localStorage;
        case StorageType.SessionStorage:
            return window.sessionStorage;
    }
}

const Storage = {
    get:(key: string, storageType = StorageType.LocalStorage): string | null => {
        const storage = getStorageFromType(storageType);
        return storage.getItem(createStorageKey(key));
    },
    set: (key: string, value: string, storageType = StorageType.LocalStorage): void => {
        const storage = getStorageFromType(storageType);
        storage.setItem(createStorageKey(key), value);
    },
    remove: (key: string, storageType = StorageType.LocalStorage): void => {
        const storage = getStorageFromType(storageType);
        storage.removeItem(createStorageKey(key));
    }
}

export { Storage, StorageType };
