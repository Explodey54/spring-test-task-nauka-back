import {Dictionary} from "../types/Dictionary";

export function findByPath (obj: Dictionary, path: string) {
    return path.split(".").reduce((o, key) => o && o[key] ? o[key] : null, obj)
}
