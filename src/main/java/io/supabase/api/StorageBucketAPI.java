package io.supabase.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.BucketUpdateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.utils.FileSize;
import io.supabase.utils.MessageResponse;
import io.supabase.utils.RestUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StorageBucketAPI implements IStorageBucketAPI {

    /**
     * <p>The url for your project.</p>
     * Example: {@code https://<PROJECT_ID>.supabase.co/storage/v1/}
     */
    private final String url;

    private final Map<String, String> headers;

    public StorageBucketAPI(String url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }


    /**
     * <p>POST /bucket/</p>
     *
     * @param bucketId The name/id of the bucket you want to create.
     * @return a {@link CreateBucketResponse} object.
     */
    @Override
    public CompletableFuture<CreateBucketResponse> createBucket(String bucketId) {
        return createBucket(bucketId, new BucketCreateOptions(false, new FileSize(0), null));
    }

    /**
     * <p>POST /bucket/</p>
     *
     * @param bucketId The name/id of the bucket you want to create.
     * @param options  The options you want to pass for the bucket creation.
     * @return a {@link CreateBucketResponse} object.
     */
    @Override
    public CompletableFuture<CreateBucketResponse> createBucket(String bucketId, BucketCreateOptions options) {
        JsonObject body = new JsonObject();
        JsonArray allowedMimeTypes = new JsonArray();
        for (String mimeType : options.getAllowedMimeTypes()) {
            allowedMimeTypes.add(mimeType);
        }
        body.addProperty("name", bucketId);
        body.addProperty("id", bucketId);
        body.addProperty("public", options.isPublic());
        body.addProperty("file_size_limit", options.getFileSizeLimit().getFileSizeAsB());
        body.add("allowed_mime_types", allowedMimeTypes);
        return RestUtils.post(new TypeToken<CreateBucketResponse>() {
        }, headers, url + "bucket", body);
    }

    /**
     * <p>GET /bucket/</p>
     * Lists all buckets in the project.
     *
     * @return A list of all buckets.
     */
    @Override
    public CompletableFuture<List<Bucket>> listBuckets() {
        return RestUtils.get(new TypeToken<List<Bucket>>() {
        }, headers, url + "bucket");
    }

    /**
     * <p>POST /bucket/{bucketId}/empty</p>
     *
     * @param bucketId The bucket you want to empty.
     * @return a status message
     */
    @Override
    public CompletableFuture<MessageResponse> emptyBucket(String bucketId) {
        String urlPath = String.format("bucket/%s/empty", bucketId);
        return RestUtils.post(new TypeToken<MessageResponse>() {
        }, headers, url + urlPath, null);
    }

    /**
     * <p>GET /bucket/{bucketId}/</p>
     *
     * @param bucketId The bucket of which you want to retrieve.
     * @return the asked for bucket
     */
    @Override
    public CompletableFuture<Bucket> getBucket(String bucketId) {
        String urlPath = String.format("bucket/%s", bucketId);
        return RestUtils.get(new TypeToken<Bucket>() {
        }, headers, url + urlPath);
    }

    /**
     * <p>PUT /bucket/{bucketId}/</p>
     *
     * @param bucketId The bucket you want to update
     * @param options  The options to update the bucket to.
     * @return a status message
     */
    @Override
    public CompletableFuture<MessageResponse> updateBucket(String bucketId, BucketUpdateOptions options) {
        JsonObject body = new JsonObject();
        body.addProperty("public", options.isPublic());
        String path = String.format("bucket/%s", bucketId);
        return RestUtils.put(new TypeToken<MessageResponse>() {
        }, headers, url + path, body);
    }

    /**
     * <p>DELETE /bucket/{bucketId}</p>
     *
     * @param bucketId The bucket of which you want to delete.
     * @return a status message
     */
    @Override
    public CompletableFuture<MessageResponse> deleteBucket(String bucketId) {
        String path = String.format("bucket/%s", bucketId);
        return RestUtils.delete(new TypeToken<MessageResponse>() {
        }, headers, url + path, null);
    }
}
