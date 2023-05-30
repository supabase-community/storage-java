package io.supabase.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSize implements JsonDeserializer<FileSize> {
    private long fileSize;

    public FileSize(String fileSizeWithUnit) {
        Matcher matcher = Pattern.compile("(\\d+)(B|KB|MB|GB)", Pattern.CASE_INSENSITIVE).matcher(fileSizeWithUnit);
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid file size format");
        int value = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2).toUpperCase();

        switch (unit) {
            case "B" -> fileSize = value;
            case "KB" -> fileSize = value * 1000L;
            case "MB" -> fileSize = value * 1000L * 1000L;
            case "GB" -> fileSize = value * 1000L * 1000L * 1000L;
        }
    }

    public FileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSizeAsB() {
        return fileSize + "B";
    }

    public String getFileSizeAsKB() {
        return (fileSize / 1000L) + "KB";
    }

    public String getFileSizeAsMB() {
        return (fileSize / 1000L / 1000L) + "MB";
    }

    public String getFileSizeAsGB() {
        return (fileSize / 1000L / 1000L / 1000L) + "GB";
    }

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public FileSize deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new FileSize(jsonElement.getAsLong());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileSize fileSize1 = (FileSize) o;
        return fileSize == fileSize1.fileSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSize);
    }
}
