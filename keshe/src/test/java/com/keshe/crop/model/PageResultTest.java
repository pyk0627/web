package com.keshe.crop.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageResultTest {

    @Test
    void calculatesPaginationInformation() {
        PageResult<String> page = new PageResult<>(List.of("a", "b"), 2, 2, 5);

        assertEquals(3, page.getTotalPages());
        assertEquals(3, page.getStartIndex());
        assertEquals(4, page.getEndIndex());
        assertTrue(page.isHasPrevious());
        assertTrue(page.isHasNext());
        assertEquals(1, page.getPreviousPage());
        assertEquals(3, page.getNextPage());
        assertEquals(2, page.getPage());
    }

    @Test
    void handlesFirstPageAndEmptyResult() {
        PageResult<String> empty = new PageResult<>(List.of(), 1, 10, 0);

        assertEquals(1, empty.getTotalPages());
        assertEquals(0, empty.getStartIndex());
        assertEquals(0, empty.getEndIndex());
        assertTrue(empty.isEmpty());
        assertFalse(empty.isHasPrevious());
        assertFalse(empty.isHasNext());
    }

    @Test
    void clampsNextPageAtTheLastPage() {
        PageResult<String> last = new PageResult<>(List.of("x"), 4, 3, 10);

        assertEquals(4, last.getTotalPages());
        assertEquals(4, last.getNextPage());
        assertFalse(last.isHasNext());
    }
}
