package com.keshe.crop.model;

import java.util.List;

/**
 * 简单分页结果。
 */
public class PageResult<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long total;

    public PageResult(List<T> content, int page, int size, long total) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public int getTotalPages() {
        if (size <= 0) {
            return 1;
        }
        return Math.max(1, (int) Math.ceil((double) total / size));
    }

    public boolean isHasPrevious() {
        return page > 1;
    }

    public boolean isHasNext() {
        return page < getTotalPages();
    }

    public int getPreviousPage() {
        return Math.max(1, page - 1);
    }

    public int getNextPage() {
        return Math.min(getTotalPages(), page + 1);
    }

    public long getStartIndex() {
        if (total == 0) {
            return 0;
        }
        return (long) (page - 1) * size + 1;
    }

    public long getEndIndex() {
        return Math.min((long) page * size, total);
    }

    public boolean isEmpty() {
        return total == 0;
    }
}
