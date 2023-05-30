package io.supabase.api;


import io.supabase.StorageClient;
import io.supabase.data.bucket.Bucket;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.bucket.BucketUpdateOptions;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.errors.StorageException;
import io.supabase.utils.FileSize;
import io.supabase.utils.MessageResponse;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.github.jsonSnapshot.SnapshotMatcher.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StorageBucketAPITest {
    private static StorageClient client;
    private static final String newBucketName = "new-bucket-name-" + LocalDateTime.now();

    @BeforeAll
    public static void initialize() {
        start();
        client = new StorageClient("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoic2VydmljZV9yb2xlIiwiaWF0IjoxNjEzNTMxOTg1LCJleHAiOjE5MjkxMDc5ODV9.FhK1kZdHmWdCIEZELt0QDCw6FIlCS8rVmp4RzaeI2LM", "http://localhost:8000/storage/v1/");
    }

    @AfterAll
    public static void afterAll() {
        validateSnapshots();
    }

    @Test
    public void listBuckets() throws ExecutionException, InterruptedException {
        List<Bucket> future = client.listBuckets().get();

        assertTrue(future.size() > 0);
    }

    @Test
    public void getBucketById() throws ExecutionException, InterruptedException {
        Bucket future = client.getBucket("bucket2").get();

        expect(future).toMatchSnapshot();
    }

    @Test
    public void getBucketThrowsIfNotFound() {
        assertThrows(StorageException.class, () -> {
            try {
                client.getBucket("not-exists-id").get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }, "Not found");
    }

    @Test
    @Order(1)
    public void createBucketSuccessfully() throws ExecutionException, InterruptedException {
        CreateBucketResponse response = client.createBucket(newBucketName).get();
        assertEquals(response.getName(), newBucketName);

    }

    @Test
    public void createPublicBucketSuccessfully() throws ExecutionException, InterruptedException {
        String bucketName = "my-new-public-bucket" + LocalDateTime.now();
        FileSize fileSizeLimit = new FileSize(0);
        List<String> mimeTypes = List.of("image/png", "image/jpeg");
        client.createBucket(bucketName, new BucketCreateOptions(true, fileSizeLimit, mimeTypes)).get();
        Bucket actual = client.getBucket(bucketName).get();

        assertTrue(actual.isBucketPublic());
        assertFalse(actual.allowedMimeTypes().isEmpty());
        assertTrue(actual.allowedMimeTypes().containsAll(mimeTypes));
        Assertions.assertEquals(fileSizeLimit.getFileSize(), actual.fileSizeLimit().getFileSize());
    }

    @Test
    @Order(2)
    public void updateBucketSuccessfully() throws ExecutionException, InterruptedException {
        MessageResponse updateResponse = client.updateBucket(newBucketName, new BucketUpdateOptions(true, new FileSize(0), null)).get();
        expect(updateResponse).toMatchSnapshot();
        Bucket bucket = client.getBucket(newBucketName).get();
        assertTrue(bucket.isBucketPublic());
    }

    @Test
    @Order(3)
    public void emptyBucket() throws ExecutionException, InterruptedException {
        MessageResponse message = client.emptyBucket(newBucketName).get();
        expect(message).toMatchSnapshot();
    }

    @Test
    @Order(4)
    public void deleteBucket() throws ExecutionException, InterruptedException {
        MessageResponse response = client.deleteBucket(newBucketName).get();
        expect(response).toMatchSnapshot();
    }

    @Test
    public void deleteBucketThrowsIfNotEmpty() {
        assertThrows(StorageException.class, () -> {
            try {
                client.deleteBucket("bucket2").get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }, "Storage not empty");
    }
}
