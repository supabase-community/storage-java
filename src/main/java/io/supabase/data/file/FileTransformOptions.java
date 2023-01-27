package io.supabase.data.file;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileTransformOptions {
    private final int width;
    private final int height;
    private ResizeOption resizeOption;
    private final int quality;
    private FormatOption format;

    public FileTransformOptions(int width, int height, ResizeOption resizeOption, int quality, FormatOption format) {
        this.width = width;
        this.height = height;
        this.resizeOption = resizeOption;
        this.quality = quality;
        this.format = format;

        if (resizeOption == null) {
            this.resizeOption = ResizeOption.COVER;
        }

        if (width <= 0 || width > 2500) {
            throw new IllegalArgumentException("An invalid width size was passed. " + width + " - Width needs to be a value between 1-2500");
        }

        if (height <= 0 || height > 2500) {
            throw new IllegalArgumentException("An invalid height size was passed. " + height + " - Height needs to be a value between 1-2500");
        }

        if (quality < 20 || quality > 100) {
            throw new IllegalArgumentException("An invalid quality value was passed. " + quality + " - Quality needs to be a value between 20-100");
        }

        if (format == null) {
            this.format = FormatOption.NONE;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResizeOption getResizeOption() {
        return resizeOption;
    }

    public int getQuality() {
        return quality;
    }

    public FormatOption getFormat() {
        return format;
    }

    public Map<String, String> convertToMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("resize", this.getResizeOption().getValue());
        map.put("width", Integer.toString(this.getWidth()));
        map.put("height", Integer.toString(this.getHeight()));
        map.put("quality", Integer.toString(this.getQuality()));
        if (this.getFormat() != FormatOption.NONE) map.put("format", this.getFormat().toString().toLowerCase());

        return map;
    }
}
