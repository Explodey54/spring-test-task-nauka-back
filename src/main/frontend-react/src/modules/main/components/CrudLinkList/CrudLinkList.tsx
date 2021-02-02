import CrudRoutes from "../../CrudRoutes";
import React from "react";
import {Link} from "react-router-dom";
import styles from "./CrudLinkList.module.scss"

export default function CrudLinkList() {
    const links = CrudRoutes.map(i =>
        <Link className={styles.link} to={`/${i.baseApi}`} key={i.baseApi}>
            {i.baseApi}
        </Link>
    );
    return <div>{links}</div>;
}
