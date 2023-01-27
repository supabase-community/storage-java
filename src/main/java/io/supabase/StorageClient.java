package io.supabase;

import io.supabase.api.IStorageFileAPI;
import io.supabase.api.StorageBucketAPI;
import io.supabase.api.StorageFileAPI;

import java.util.HashMap;
import java.util.Map;

public class StorageClient extends StorageBucketAPI implements IStorageClient {
    private final String url;
    private final Map<String, String> headers;

    public StorageClient(String apiKey, String url) {
        this(url, new HashMap<>() {{
            put("Authorization", "Bearer " + apiKey);
        }});
        // Validate URL and throw if not a valid url.
    }

    private StorageClient(String url, Map<String, String> headers) {
        super(url, headers);
        this.url = url;
        this.headers = headers;
    }

    @Override
    public IStorageFileAPI from(String bucketId) {
        return new StorageFileAPI(this.url, this.headers, bucketId);
    }
}
