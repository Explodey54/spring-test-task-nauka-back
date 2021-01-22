import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  get(key: string, storageType = StorageType.LocalStorage): string | null {
    const storage = this.getStorageFromType(storageType);
    return storage.getItem(key);
  }

  set(key: string, value: string, storageType = StorageType.LocalStorage): void {
    const storage = this.getStorageFromType(storageType);
    storage.setItem(key, value);
  }

  private getStorageFromType(type: StorageType): Storage {
    switch (type) {
      case StorageType.LocalStorage:
        return window.localStorage;
      case StorageType.SessionStorage:
        return window.sessionStorage;
    }
  }


}

export enum StorageType {
  LocalStorage,
  SessionStorage
}
