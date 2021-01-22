package com.testtask.nauka.common.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UtilHelpers {
    /**
     * Function that checks if obj is not null and returns result of the applied function.
     * If obj equals null, then returns null.
     * @param target target object
     * @param func desired result
     * @param <T> type of target
     * @param <R> type of function result
     * @return null or result of the function
     */
    public static <T, R> R ifNotNull(T target, Function<T, R> func) {
        return target == null ? null : func.apply(target);
    }

    /**
     * Function that checks if obj is not null and executes consumer function, with obj as parameter.
     * If obj equals null, then nothing happens.
     * @param target target object
     * @param func desired result
     * @param <T> type of target
     */
    public static <T> void ifNotNullDo(T target, Consumer<T> func) {
        if (target != null) func.accept(target);
    }
}
