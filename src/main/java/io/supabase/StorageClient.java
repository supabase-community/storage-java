package io.supabase;

import io.supabase.api.IStorageFileAPI;
import io.supabase.api.StorageBucketAPI;

public class StorageClient extends StorageBucketAPI implements IStorageClient {


    public StorageClient(String apiKey, String url) {
        super(apiKey, url);
        // Validate URL and throw if not a valid url.
    }

    @Override
    public IStorageFileAPI from(String bucketId) {
        return null;
    }
}
