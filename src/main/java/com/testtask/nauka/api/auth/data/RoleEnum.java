package com.testtask.nauka.api.auth.data;

public enum RoleEnum {
    ADMIN (1),
    MANAGER_DEPARTMENTS (2),
    MANAGER_WORKERS (2),
    TIMEKEEPER (3),
    GUEST (4);

    private final int access;

    private RoleEnum(int access) {
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
}
