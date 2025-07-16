package com.backendapp.cms.common.enums;

public enum SortBy {
    USERNAME("username"),
    CREATEDAT("createdAt"),
    DELETEDAT("deletedAt"),
    PUBLISHED("deletedAt");

    public final String value;

    SortBy(String value) {
        this.value = value;
    }
}
