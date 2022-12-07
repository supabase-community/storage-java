package io.supabase;


import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.errors.StorageException;
import io.supabase.utils.MessageResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class StorageBucketAPITest {
    private static StorageClient client;

    @BeforeAll
    public static void initialize() throws ExecutionException, InterruptedException {
        client = new StorageClient("", "");
        client.createBucket("testbucket").get();
        // add files to bucket to make deleteBucket tests work.
    }

    @Test
    public void createBucket() throws ExecutionException, InterruptedException {
        CreateBucketResponse message = client.createBucket("firstbucket").get();
        client.createBucket("secondbucket", new BucketCreateOptions(true)).get();

        Bucket actual = client.getBucket("secondbucket").get();
        assertEquals(message.getName(), "firstbucket");
        assertTrue(actual.isBucketPublic());

        // cleanup
        client.deleteBucket("firstbucket").get();
        client.deleteBucket("secondbucket").get();
    }

    @Test
    public void listBuckets() throws ExecutionException, InterruptedException {
        List<Bucket> future = client.listBuckets().get();

        assertEquals(1, future.size());
    }

    @Test
    public void getBucket() throws ExecutionException, InterruptedException {
        Bucket future = client.getBucket("testbucket").get();

        assertNotNull(future);
    }

    @Test
    public void getBucketThrowsIfNotFound() {
        assertThrows(StorageException.class, () -> {
            try {
                client.getBucket("thisbucketcertainlydoesnotexist").get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }, "Not found");
    }

    @Test
    public void emptyBucket() throws ExecutionException, InterruptedException {
        MessageResponse message = client.emptyBucket("testbucket").get();

        assertEquals(message.getMessage(), "Successfully emptied");
    }

    @Test
    public void deleteBucketThrowsIfNotEmpty() {

        assertThrows(StorageException.class, () -> {
            try {
                client.deleteBucket("testbucket").get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }, "Storage not empty");
    }
}
