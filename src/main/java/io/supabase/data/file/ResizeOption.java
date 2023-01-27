package io.supabase.data.file;

public enum ResizeOption {
    /**
     * Default value
     */
    COVER("cover"),
    CONTAIN("contain"),
    FILL("fill");

    private final String value;
    ResizeOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
