package io.supabase.data.bucket;

public class CreateBucketResponse {
    public String name;

    public CreateBucketResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
