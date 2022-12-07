package io.supabase.data.bucket;

public class BucketOptions {
    private final boolean isPublic;

    public BucketOptions(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
