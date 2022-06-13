package com.endava.endabank.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class Pagination {
    public static final int DEFAULT_PAGE_SIZE = 10;

    public Pageable getPageable(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public Pageable getPageable(int page, Sort sort) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);
    }

    public Pageable getPageable(int page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE);
    }
}
