<p align="center">
<img width="300" src=".github/supabase-storage.png"/>
</p>

# storage-java
An async Java client library for the [Supabase Storage API](https://github.com/supabase/storage-api)

The version being used supports different version of the storage API, you can find which version supports up to what version in the [CHANGELOG](./CHANGELOG.md)

## Example

```java
import io.supabase.StorageClient;
import io.supabase.api.IStorageFileAPI;
import io.supabase.data.bucket.CreateBucketResponse;
import io.supabase.data.file.*;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        String url = System.getenv("SUPABASE_URL");
        String serviceToken = System.getenv("SUPABASE_SERVICE_TOKEN");

        StorageClient storageClient = new StorageClient(serviceToken, url);

        // Interact with Supabase Storage
        CompletableFuture<CreateBucketResponse> res = storageClient.createBucket("examplebucket");

        // Do something on future completion.
        res.thenAccept((bucketRes) -> {
            IStorageFileAPI fileAPI = storageClient.from(bucketRes.getName());
            try {
                // We call .get here to block the thread and retrieve the value or an exception.
                // Pass the file path in supabase storage and pass a file object of the file you want to upload.
                FilePathResponse response = fileAPI.upload("my-secret-image/image.png", new File("file-path-to-image.png")).get();

                // Generate a public url (The link is only valid if the bucket is public).
                fileAPI.getPublicUrl("my-secret-image/image.png", new FileDownloadOption(false), new FileTransformOptions(500, 500, ResizeOption.COVER, 50, FormatOption.NONE));

                // Create a signed url to download an object in a private bucket that expires in 60 seconds, and will be downloaded instantly on link as "my-image.png"
                fileAPI.getSignedUrl("my-secret-image/image.png", 60, new FileDownloadOption("my-image.png"), null);

                // Download the file
                fileAPI.download("my-secret-image/image.png", null);

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

```

## Package made possible through the efforts of: 
<a href="https://github.com/supabase-community/storage-java/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=supabase-community/storage-java" />
</a>

Made with [contrib.rocks](https://contrib.rocks).

## Contributing

We are more than happy to have contributions! Please submit a PR.

### Local development
For testing the application you will need to spin up the docker compose found in the `infra` folder.
<br>
1. `cd infra`.
2. `cp .env.example .env`
3. Fill in the values of the .env
4. `docker compose up -d`
<p>Then you can test the application with the default values already in the test files.</p> 
