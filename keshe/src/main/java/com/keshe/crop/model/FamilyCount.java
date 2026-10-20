package com.keshe.crop.model;

/**
 * 首页科属统计。
 */
public class FamilyCount {

    private final String name;
    private final long count;

    public FamilyCount(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }
}
