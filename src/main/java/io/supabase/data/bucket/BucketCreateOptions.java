package io.supabase.data.bucket;

import io.supabase.utils.FileSize;

import java.util.List;

public class BucketCreateOptions extends BucketOptions {
    public BucketCreateOptions(boolean isPublic, FileSize fileSizeLimit, List<String> allowedMimeTypes) {
        super(isPublic, fileSizeLimit, allowedMimeTypes == null ? List.of() : allowedMimeTypes);
    }
}
