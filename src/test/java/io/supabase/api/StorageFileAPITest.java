package io.supabase.api;

import io.supabase.StorageClient;
import io.supabase.data.bucket.BucketCreateOptions;
import io.supabase.data.file.*;
import io.supabase.utils.FileSize;
import io.supabase.utils.MessageResponse;
import io.supabase.utils.Utilities;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StorageFileAPITest {
    private static final String URL = "http://localhost:8000/storage/v1/";
    private static StorageClient client;

    private static String bucketName = "";
    private static File file;
    private static String uploadPath;

    private static String newBucket(boolean isPublic) throws ExecutionException, InterruptedException {
        String bucketName = "bucket-" + LocalDateTime.now();
        client.createBucket(bucketName, new BucketCreateOptions(isPublic, new FileSize(0), null)).get();
        return bucketName;
    }

    @BeforeAll
    public static void initialize() {
        client = new StorageClient("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoic2VydmljZV9yb2xlIiwiaWF0IjoxNjEzNTMxOTg1LCJleHAiOjE5MjkxMDc5ODV9.FhK1kZdHmWdCIEZELt0QDCw6FIlCS8rVmp4RzaeI2LM", URL);
    }

    @BeforeEach
    public void beforeEach() throws ExecutionException, InterruptedException {
        bucketName = newBucket(true);
        file = new File("src/test/resources/io/supabase/file.txt");
        uploadPath = "testpath/file-" + LocalDateTime.now() + ".png";
    }

    @AfterAll
    public static void afterAll() {
    }

    @Test
    public void getPublicUrl() {
        FilePublicUrlResponse res = client.from(bucketName).getPublicUrl(uploadPath, new FileDownloadOption(false), null);
        assertEquals(URL + "object/public/" + bucketName + "/" + uploadPath, res.getPublicUrl());
    }

    @Test
    public void getPublicUrlWithDownloadQuery() {
        FileDownloadOption downloadOptions = new FileDownloadOption(true);
        FilePublicUrlResponse res = client.from(bucketName).getPublicUrl(uploadPath, downloadOptions, null);
        assertEquals(URL + "object/public/" + bucketName + "/" + uploadPath + Utilities.convertMapToQueryParams(downloadOptions.convertToMap()), res.getPublicUrl());
    }

    @Test
    public void getPublicUrlWithCustomDownloadQuery() {
        FileDownloadOption downloadOptions = new FileDownloadOption("test.jpg");
        FilePublicUrlResponse res = client.from(bucketName).getPublicUrl(uploadPath, downloadOptions, null);
        assertEquals(URL + "object/public/" + bucketName + "/" + uploadPath + Utilities.convertMapToQueryParams(downloadOptions.convertToMap()), res.getPublicUrl());
    }

    @Test
    public void getPublicUrlWithTransformOptions() {
        FileTransformOptions transformOptions = new FileTransformOptions(500, 500, null, 20, FormatOption.NONE);
        FilePublicUrlResponse res = client.from(bucketName).getPublicUrl(uploadPath, null, transformOptions);
        assertTrue(res.getPublicUrl().contains(URL + "render/image/public/" + bucketName + "/" + uploadPath));
        for (String keyVal : Utilities.convertMapToQueryParams(transformOptions.convertToMap()).replace("?", "").split("&")) {
            assertTrue(res.getPublicUrl().contains(keyVal));
        }

    }

    @Test
    public void signUrl() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FileSignedUrlResponse res = client.from(bucketName).getSignedUrl(uploadPath, 2000, new FileDownloadOption(false), null).get();
        assertTrue(res.getSignedUrl().contains(String.format("%s/object/sign/%s/%s", URL, bucketName, uploadPath)));
    }

    @Test
    public void signUrlWithDownloadParams() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FileSignedUrlResponse res = client.from(bucketName).getSignedUrl(uploadPath, 2000, new FileDownloadOption(true), null).get();
        assertTrue(res.getSignedUrl().contains(String.format("%s/object/sign/%s/%s", URL, bucketName, uploadPath)));
        assertTrue(res.getSignedUrl().contains("?download="));
    }

    @Test
    public void signUrlWithCustomDownloadName() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FileSignedUrlResponse res = client.from(bucketName).getSignedUrl(uploadPath, 2000, new FileDownloadOption("test.jpg"), null).get();
        assertTrue(res.getSignedUrl().contains(String.format("%s/object/sign/%s/%s", URL, bucketName, uploadPath)));
        assertTrue(res.getSignedUrl().contains("?download=test.jpg"));
    }

    @Test
    public void signUrlWithTransformParams() throws ExecutionException, InterruptedException {
        FileTransformOptions transformOptions = new FileTransformOptions(500, 500, ResizeOption.COVER, 20, FormatOption.NONE);
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FileSignedUrlResponse res = client.from(bucketName).getSignedUrl(uploadPath, 2000, null, transformOptions).get();
        assertTrue(res.getSignedUrl().contains(String.format("%s/object/sign/%s/%s", URL, bucketName, uploadPath)));
        for (String keyVal : Utilities.convertMapToQueryParams(transformOptions.convertToMap()).replace("?", "").split("&")) {
            assertTrue(res.getSignedUrl().contains(keyVal));
        }
    }

    @Test
    public void uploadFile() throws ExecutionException, InterruptedException {
        newBucket(true);
        FilePathResponse response = client.from(StorageFileAPITest.bucketName).upload(uploadPath, file).get();
        assertNotNull(response);
        assertEquals(bucketName + "/" + uploadPath, response.getKey());
    }

    @Test
    public void uploadAndUpdateFile() throws ExecutionException, InterruptedException {
        File file2 = new File("src/test/resources/io/supabase/file-2.txt");

        FilePathResponse response = client.from(bucketName).upload(uploadPath, file).get();
        assertNotNull(response);

        FilePathResponse updateResponse = client.from(bucketName).update(uploadPath, file2).get();
        assertEquals(bucketName + "/" + uploadPath, updateResponse.getKey());
    }

    // Something weird about this test, does not work half the times when ran in pipeline
    @Disabled
    public void listFiles() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        List<io.supabase.data.file.File> files = client.from(bucketName).list("testpath", FileSearchOptions.DEFAULT).get();

        assertNotNull(files);
        assertTrue(files.size() > 0);
        assertEquals(uploadPath.replace("testpath/", ""), files.get(0).getName());
    }

    @Test
    public void moveFile() throws ExecutionException, InterruptedException {
        String newPath = "testpath/file-moved-" + LocalDateTime.now() + ".txt";
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        MessageResponse res = client.from(bucketName).move(uploadPath, newPath).get();

        assertEquals("Successfully moved", res.getMessage());
    }

    @Test
    public void copyFile() throws ExecutionException, InterruptedException {
        String newPath = "testpath/file-copied-" + LocalDateTime.now() + ".txt";
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FilePathResponse res = client.from(bucketName).copy(uploadPath, newPath).get();

        assertEquals(bucketName + "/" + newPath, res.getKey());
    }

    @Test
    public void downloadFile() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        FileDownload fileContents = client.from(bucketName).download(uploadPath, null).get();
        assertNotNull(fileContents);
        assertTrue(fileContents.getBytes().length > 0);
        assertEquals("text/plain; charset=UTF-8", fileContents.getType().toString());
    }

    @Test
    public void deleteFile() throws ExecutionException, InterruptedException {
        newBucket(true);
        client.from(bucketName).upload(uploadPath, file).get();
        List<String> paths = new ArrayList<>();
        paths.add(uploadPath);
        List<io.supabase.data.file.File> filesDeleted = client.from(bucketName).delete(paths).get();

        assertTrue(filesDeleted.size() > 0);
        assertEquals(bucketName, filesDeleted.get(0).getBucketId());
        assertEquals(uploadPath, filesDeleted.get(0).getName());
    }
}
