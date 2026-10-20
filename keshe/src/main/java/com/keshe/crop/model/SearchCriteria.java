package com.keshe.crop.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 组合查询条件。
 */
public class SearchCriteria {

    private String keyword;
    private String family;
    private String genus;
    private BigDecimal oilMin;
    private BigDecimal oilMax;
    private boolean onlyWithOil;
    private String sort = "name";
    private int page = 1;
    private int size = 10;
    private List<String> fields = new ArrayList<>();

    public void normalize() {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        if (size > 50) {
            size = 50;
        }
        if (sort == null || sort.isBlank()) {
            sort = "name";
        }
        if (keyword != null) {
            keyword = keyword.trim();
        }
        if (family != null) {
            family = family.trim();
        }
        if (genus != null) {
            genus = genus.trim();
        }
    }

    /**
     * 生成保留查询条件和已选字段的分页链接，例如：
     * keyword=松&amp;family=松科&amp;page=2&amp;size=10&amp;sort=name&amp;fields=chineseName
     */
    public String toQueryString(int targetPage) {
        List<String> parts = new ArrayList<>();
        add(parts, "keyword", keyword);
        add(parts, "family", family);
        add(parts, "genus", genus);
        add(parts, "oilMin", oilMin);
        add(parts, "oilMax", oilMax);
        if (onlyWithOil) {
            add(parts, "onlyWithOil", "true");
        }
        add(parts, "sort", sort);
        add(parts, "size", size);
        add(parts, "page", targetPage);
        if (fields != null) {
            for (String field : fields) {
                add(parts, "fields", field);
            }
        }
        return String.join("&", parts);
    }

    public String toQueryString() {
        return toQueryString(page);
    }

    private void add(List<String> parts, String name, Object value) {
        if (value == null) {
            return;
        }
        String text = String.valueOf(value);
        if (text.isBlank()) {
            return;
        }
        try {
            parts.add(name + "=" + URLEncoder.encode(text, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            parts.add(name + "=" + text);
        }
    }

    public boolean isHasFilter() {
        return (keyword != null && !keyword.isBlank())
                || (family != null && !family.isBlank())
                || (genus != null && !genus.isBlank())
                || oilMin != null
                || oilMax != null
                || onlyWithOil;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public BigDecimal getOilMin() {
        return oilMin;
    }

    public void setOilMin(BigDecimal oilMin) {
        this.oilMin = oilMin;
    }

    public BigDecimal getOilMax() {
        return oilMax;
    }

    public void setOilMax(BigDecimal oilMax) {
        this.oilMax = oilMax;
    }

    public boolean isOnlyWithOil() {
        return onlyWithOil;
    }

    public void setOnlyWithOil(boolean onlyWithOil) {
        this.onlyWithOil = onlyWithOil;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
