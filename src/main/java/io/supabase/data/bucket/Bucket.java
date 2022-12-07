package io.supabase.data.bucket;


import com.google.gson.annotations.SerializedName;

public class Bucket {
    private final String id;
    private final String name;
    private final String owner;
    @SerializedName("public")
    private final boolean isBucketPublic;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("updated_at")
    private final String updatedAt;

    public Bucket(String id, String name, String owner, boolean isBucketPublic, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.isBucketPublic = isBucketPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isBucketPublic() {
        return isBucketPublic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
