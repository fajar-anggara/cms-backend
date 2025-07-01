package com.backendapp.cms.common.enums;

import org.springframework.data.domain.Sort;

public enum SortOrder {
    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC);

    public final Sort.Direction direction;

    SortOrder(Sort.Direction direction) {
        this.direction = direction;
    }
}
