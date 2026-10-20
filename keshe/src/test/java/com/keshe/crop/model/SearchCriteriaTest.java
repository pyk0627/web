package com.keshe.crop.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchCriteriaTest {

    @Test
    void normalizesUnsafePagingValues() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setPage(0);
        criteria.setSize(999);
        criteria.setSort(" ");

        criteria.normalize();

        assertEquals(1, criteria.getPage());
        assertEquals(50, criteria.getSize());
        assertEquals("name", criteria.getSort());
    }

    @Test
    void trimsTextFilters() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKeyword("  松  ");
        criteria.setFamily("  松科 ");
        criteria.setGenus(" 松属  ");

        criteria.normalize();

        assertEquals("松", criteria.getKeyword());
        assertEquals("松科", criteria.getFamily());
        assertEquals("松属", criteria.getGenus());
    }

    @Test
    void buildsEncodedQueryStringWithPagingAndFields() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKeyword("松");
        criteria.setOilMin(new BigDecimal("20.5"));
        criteria.setOnlyWithOil(true);
        criteria.setFields(List.of("chineseName", "oil"));

        String query = criteria.toQueryString(3);

        assertTrue(query.contains("keyword=%E6%9D%BE"));
        assertTrue(query.contains("oilMin=20.5"));
        assertTrue(query.contains("onlyWithOil=true"));
        assertTrue(query.contains("page=3"));
        assertTrue(query.contains("fields=chineseName"));
        assertTrue(query.contains("fields=oil"));
    }

    @Test
    void detectsWhetherAnyFilterIsActive() {
        SearchCriteria criteria = new SearchCriteria();
        assertFalse(criteria.isHasFilter());

        criteria.setOilMax(new BigDecimal("40"));
        assertTrue(criteria.isHasFilter());
    }
}
