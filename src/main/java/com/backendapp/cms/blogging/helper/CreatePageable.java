package com.backendapp.cms.blogging.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CreatePageable {

    public PageRequest get(String sortBy, Sort.Direction direction, int pageSize, int limit) {
        return PageRequest.of(pageSize, limit, Sort.by(direction, sortBy));
    }
}
