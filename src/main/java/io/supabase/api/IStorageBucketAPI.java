package io.supabase.api;

import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.BucketUpdateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.utils.MessageResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IStorageBucketAPI {

    CompletableFuture<CreateBucketResponse> createBucket(String bucketId);
    CompletableFuture<CreateBucketResponse> createBucket(String bucketId, BucketCreateOptions options);

    /**
     * Lists all buckets in the project.
     * @return A list of all buckets.
     */
    CompletableFuture<List<Bucket>> listBuckets();

    CompletableFuture<MessageResponse> emptyBucket(String bucketId);

    CompletableFuture<Bucket> getBucket(String bucketId);

    CompletableFuture<MessageResponse> updateBucket(String bucketId, BucketUpdateOptions options);

    CompletableFuture<MessageResponse> deleteBucket(String bucketId);

}
