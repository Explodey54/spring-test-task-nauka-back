import {Dictionary} from "../types/Dictionary";

export function findByPath(obj: Dictionary, path: string): any {
    if (!obj) return obj;

    const paths = path.split('.');
    let current = obj;

    for (let i = 0; i < paths.length; ++i) {
        if (current[paths[i]] == undefined) {
            return undefined;
        } else {
            current = current[paths[i]];
        }
    }
    return current;
}
