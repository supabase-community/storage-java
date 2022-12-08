package io.supabase.data.file;

public class FilePublicUrlResponse {
    private final String publicUrl;

    public FilePublicUrlResponse(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public String getPublicUrl() {
        return publicUrl;
    }
}
