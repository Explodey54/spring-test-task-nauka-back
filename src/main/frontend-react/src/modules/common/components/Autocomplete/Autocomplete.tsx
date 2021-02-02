import React, {useEffect, useState} from "react";
import BaseFetch from "../../services/BaseFetch";

interface IProps {
    name: string;
    url?: string;
    onFetch?: () => unknown;
}

interface IAutocomplete {
    id: number;
    title: string;
}

const Autocomplete = React.forwardRef<HTMLSelectElement, IProps>((props, ref) => {
    const [autocompletes, setAutocompletes] = useState<IAutocomplete[]>([]);

    const getAutocomplete = (url: string): Promise<IAutocomplete[]> => {
        return BaseFetch.fetchJson(`autocomplete/${url}`) as Promise<any>;
    };

    useEffect(() => {
        if (!props.url) throw new Error('Autocomplete missing url!');
        getAutocomplete(props.url).then(i => {
            setAutocompletes(i);
            if (props.onFetch) props.onFetch();
        });
    }, []);


    const optionList = autocompletes.map(i => (
        <option key={i.id} value={i.id}>{i.title}</option>
    ));

    return (
        <select ref={ref} name={props.name}>
            {optionList}
        </select>
    );
});

export default Autocomplete;


