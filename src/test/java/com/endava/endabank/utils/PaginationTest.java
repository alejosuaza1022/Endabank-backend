package com.endava.endabank.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaginationTest {
    @Test
    void testGetPageableShouldSuccessWhenDataCorrect(){
        Pagination pagination = new Pagination();
        Pageable pageable = pagination.getPageable(1, 20, Sort.unsorted());
        assertEquals(1, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }
    @Test
    void testGetPageableWithOutSizeShouldSuccessWhenDataCorrect(){
        Pagination pagination = new Pagination();
        Pageable pageable = pagination.getPageable(1, Sort.unsorted());
        assertEquals(1, pageable.getPageNumber());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }
    @Test
    void testGetPageableWithOutSortShouldSuccessWhenDataCorrect(){
        Pagination pagination = new Pagination();
        Pageable pageable = pagination.getPageable(1);
        assertEquals(1, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }
}
