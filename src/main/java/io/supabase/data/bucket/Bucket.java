package io.supabase.data.bucket;


import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.supabase.utils.FileSize;

import java.util.List;

public record Bucket(String id, String name, String owner,
                     @SerializedName("public") boolean isBucketPublic,
                     @SerializedName("file_size_limit") @JsonAdapter(FileSize.class) FileSize fileSizeLimit,
                     @SerializedName("allowed_mime_types") List<String> allowedMimeTypes,
                     @SerializedName("created_at") String createdAt,
                     @SerializedName("updated_at") String updatedAt) {

    @Override
    public String toString() {
        return "Bucket{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            ", isBucketPublic=" + isBucketPublic +
            ", createdAt='" + createdAt + '\'' +
            ", updatedAt='" + updatedAt + '\'' +
            '}';
    }
}
