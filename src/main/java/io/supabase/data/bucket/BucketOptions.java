package io.supabase.data.bucket;

import io.supabase.utils.FileSize;

import java.util.List;

public class BucketOptions {
    private final boolean isPublic;
    private final FileSize fileSizeLimit;
    private final List<String> allowedMimeTypes;

    public BucketOptions(boolean isPublic, FileSize fileSizeLimit, List<String> allowedMimeTypes) {
        this.isPublic = isPublic;
        this.fileSizeLimit = fileSizeLimit;
        this.allowedMimeTypes = List.copyOf(allowedMimeTypes);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public FileSize getFileSizeLimit() {
        return fileSizeLimit;
    }

    public List<String> getAllowedMimeTypes() {
        return List.copyOf(allowedMimeTypes);
    }
}
