package com.backendapp.cms.blogging.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CreatePageable {

    public PageRequest get(String sortBy, Sort.Direction direction, int page, int limit) {
        return PageRequest.of(page, limit, Sort.by(direction, sortBy));
    }
}
