package io.supabase;

import io.supabase.api.IStorageFileAPI;

public interface IStorageClient {

    /**
     * Retrieves a file api for the specified bucket id.
     * @param bucketId The bucket id to retrieve a file api for.
     * @return The retrieved file api.
     */
    IStorageFileAPI from(String bucketId);
}
