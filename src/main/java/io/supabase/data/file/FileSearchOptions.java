package io.supabase.data.file;

public class FileSearchOptions {
    public static FileSearchOptions DEFAULT = new FileSearchOptions(100, 0, new SortBy("name", "asc"));
    private final int limit;
    private final int offset;
    private final SortBy sortBy;

    public FileSearchOptions(int limit, int offset, SortBy sortBy) {
        this.limit = limit;
        this.offset = offset;
        this.sortBy = sortBy;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public static class SortBy {
        private final String column;
        private final String order;

        public SortBy(String column, String order) {
            this.column = column;
            this.order = order;
        }

        public String getColumn() {
            return column;
        }

        public String getOrder() {
            return order;
        }
    }
}


