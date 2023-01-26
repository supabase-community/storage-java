package io.supabase.api;

import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.BucketUpdateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.utils.MessageResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IStorageBucketAPI {

    /**
     * <p>POST /bucket/</p>
     *
     * @param bucketId The name/id of the bucket you want to create.
     * @return a {@link CreateBucketResponse} object.
     */
    CompletableFuture<CreateBucketResponse> createBucket(String bucketId);

    /**
     * <p>POST /bucket/</p>
     *
     * @param bucketId The name/id of the bucket you want to create.
     * @param options  The options you want to pass for the bucket creation.
     * @return a {@link CreateBucketResponse} object.
     */
    CompletableFuture<CreateBucketResponse> createBucket(String bucketId, BucketCreateOptions options);

    /**
     * <p>GET /bucket/</p>
     * Lists all buckets in the project.
     *
     * @return A list of all buckets.
     */
    CompletableFuture<List<Bucket>> listBuckets();

    /**
     * <p>POST /bucket/{bucketId}/empty</p>
     *
     * @param bucketId The bucket you want to empty.
     * @return a status message
     */
    CompletableFuture<MessageResponse> emptyBucket(String bucketId);

    /**
     * <p>GET /bucket/{bucketId}/</p>
     *
     * @param bucketId The bucket of which you want to retrieve.
     * @return the asked for bucket
     */
    CompletableFuture<Bucket> getBucket(String bucketId);

    /**
     * <p>PUT /bucket/{bucketId}/</p>
     *
     * @param bucketId The bucket you want to update
     * @param options  The options to update the bucket to.
     * @return a status message
     */
    CompletableFuture<MessageResponse> updateBucket(String bucketId, BucketUpdateOptions options);

    /**
     * <p>DELETE /bucket/{bucketId}</p>
     *
     * @param bucketId The bucket of which you want to delete.
     * @return a status message
     */
    CompletableFuture<MessageResponse> deleteBucket(String bucketId);

}
