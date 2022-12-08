package io.supabase.data.file;

import com.google.gson.annotations.SerializedName;

public class File {
    private final String name;
    @SerializedName("bucket_id")
    private final String bucketId;
    private final String owner;
    private final String id;
    @SerializedName("updated_at")
    private final String updatedAt;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("last_accessed_at")
    private final String lastAccessedAt;
    private final Metadata metadata;

    public File(String name, String bucketId, String owner, String id, String updatedAt, String createdAt, String lastAccessedAt, Metadata metadata) {
        this.name = name;
        this.bucketId = bucketId;
        this.owner = owner;
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public String getBucketId() {
        return bucketId;
    }

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastAccessedAt() {
        return lastAccessedAt;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public static class Metadata {
        private final int size;

        public Metadata(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}
