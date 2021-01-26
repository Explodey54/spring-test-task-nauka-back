import React from 'react';
import { useForm } from "react-hook-form";

export interface IProps {
    onSubmit?: (value: IAuthForm) => unknown
}

export interface IAuthForm {
    username: string;
    password: string;
}

function AuthForm(props: IProps) {
    const { register, handleSubmit } = useForm();
    const onSubmit = (data: IAuthForm) => {
        if (props.onSubmit) props.onSubmit(data);
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <label>
                <span>Username</span>
                <input type="text" name="username" ref={register}/>
            </label>
            <label>
                <span>Password</span>
                <input type="password" name="password" ref={register}/>
            </label>
            <input type="submit"/>
        </form>
    );
}

export default AuthForm;
