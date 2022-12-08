package io.supabase.data.file;

import com.google.gson.annotations.SerializedName;

public class FileSignedUrlResponse {
    @SerializedName("signedURL")
    private String signedUrl;

    public FileSignedUrlResponse(String signedUrl) {
        this.signedUrl = signedUrl;
    }

    public String getSignedUrl() {
        return signedUrl;
    }
}
