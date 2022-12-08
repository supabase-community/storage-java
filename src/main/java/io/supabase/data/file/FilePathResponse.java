package io.supabase.data.file;

import com.google.gson.annotations.SerializedName;

public class FilePathResponse {

    @SerializedName("Key")
    private final String key;

    public FilePathResponse(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
