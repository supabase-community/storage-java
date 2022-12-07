package io.supabase.api;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.BucketUpdateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.utils.MessageResponse;
import io.supabase.utils.RestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StorageBucketAPI implements IStorageBucketAPI {

    /**
     * The API Key which is used to make requests on behalf of.
     */
    private final String apiKey;

    /**
     * <p>The url for your project.</p>
     * Example: {@code https://<PROJECT_ID>.supabase.co/storage/v1/}
     */
    private final String url;

    private final Map<String, String> headers;

    public StorageBucketAPI(String apiKey, String url) {
        this.apiKey = apiKey;
        this.url = url;
        this.headers = new HashMap<>();
        this.headers.put("Authorization", "Bearer " + apiKey);
    }


    @Override
    public CompletableFuture<CreateBucketResponse> createBucket(String bucketId) {
        return createBucket(bucketId, new BucketCreateOptions(false));
    }

    @Override
    public CompletableFuture<CreateBucketResponse> createBucket(String bucketId, BucketCreateOptions options) {
        JsonObject body = new JsonObject();
        body.addProperty("name", bucketId);
        body.addProperty("id", bucketId);
        body.addProperty("public", options.isPublic());
        return RestUtils.post(new TypeToken<CreateBucketResponse>(){}, headers, url + "bucket", body);
    }

    /**
     * Lists all buckets in the project.
     *
     * @return A list of all buckets.
     */
    @Override
    public CompletableFuture<List<Bucket>> listBuckets() {
        return RestUtils.get(new TypeToken<List<Bucket>>() {
        }, headers, url + "bucket");
    }

    @Override
    public CompletableFuture<MessageResponse> emptyBucket(String bucketId) {
        String urlPath = String.format("bucket/%s/empty", bucketId);
        return RestUtils.post(new TypeToken<MessageResponse>(){}, headers, url + urlPath, null);
    }

    @Override
    public CompletableFuture<Bucket> getBucket(String bucketId) {
        String urlPath = String.format("bucket/%s", bucketId);
        return RestUtils.get(new TypeToken<Bucket>(){}, headers, url + urlPath);
    }

    @Override
    public CompletableFuture<MessageResponse> updateBucket(String bucketId, BucketUpdateOptions options) {
        JsonObject body = new JsonObject();
        body.addProperty("public", options.isPublic());
        String path = String.format("bucket/%s", bucketId);
        return RestUtils.put(new TypeToken<MessageResponse>(){}, headers, url + path, body);
    }

    @Override
    public CompletableFuture<MessageResponse> deleteBucket(String bucketId) {
        String path = String.format("bucket/%s", bucketId);
        return RestUtils.delete(new TypeToken<MessageResponse>(){}, headers, url + path, null);
    }
}
