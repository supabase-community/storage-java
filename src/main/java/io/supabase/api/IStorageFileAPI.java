package io.supabase.api;

import io.supabase.data.file.*;
import io.supabase.utils.MessageResponse;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IStorageFileAPI {

    /**
     * <p>POST /object/{bucketName}/{wildcard}</p>
     *
     * @param path The path to the file within the bucket of where it should be uploaded.
     * @param file The file that needs to be uploaded.
     * @return a {@link FilePathResponse}
     */
    CompletableFuture<FilePathResponse> upload(String path, File file);

    /**
     * <p>PUT /object/{bucketName}/{wildcard}</p>
     *
     * @param path The path to the file within the bucket that should get updated.
     * @param file The new file that should be placed.
     * @return a {@link FilePathResponse}
     */
    CompletableFuture<FilePathResponse> update(String path, File file);

    /**
     * <p>POST /object/move/</p>
     *
     * @param fromPath The path to the object that needs to be moved.
     * @param toPath   The new path where the object should be moved to.
     * @return a {@link MessageResponse}
     */
    CompletableFuture<MessageResponse> move(String fromPath, String toPath);

    /**
     * <p>POST /object/copy/</p>
     *
     * @param fromPath The path to the object that needs to be copied.
     * @param toPath   The new path where the object should be copied to.
     * @return a {@link FilePathResponse}
     */
    CompletableFuture<FilePathResponse> copy(String fromPath, String toPath);

    /**
     * <p>POST /object/sign/{bucketName}</p>
     * <p>This method just wraps {@link #getSignedUrls(List, int, FileDownloadOption)}</p>
     *
     * @param path      The singular file path that should be signed.
     * @param expiresIn how many seconds until the signed url expires.
     * @param options   any additional download options.
     * @return a {@link FileSignedUrlResponse}
     */
    CompletableFuture<FileSignedUrlResponse> getSignedUrl(String path, int expiresIn, FileDownloadOption options);

    /**
     * <p>POST /object/sign/{bucketName}</p>
     *
     * @param paths     a list of file paths that should be signed.
     * @param expiresIn how many seconds until the signed urls expires.
     * @param options   any additional download options.
     * @return a list of {@link FileSignedUrlResponse}
     */
    CompletableFuture<List<FileSignedUrlResponse>> getSignedUrls(List<String> paths, int expiresIn, FileDownloadOption options);

    /**
     * <p>Downloads a file from a private bucket. To download something from a public bucket, make a request to the url from {@link #getPublicUrl(String, FileDownloadOption)}</p>
     * <p>GET /object/authenticated/{bucketName}/{wildcard}</p>
     *
     * @param path The path of the file to download
     * @return a {@link FileDownload}
     */
    CompletableFuture<FileDownload> download(String path);

    /**
     * Creates the url for an object in a public bucket
     *
     * @param path    The path of the file the link should point to.
     * @param options The download options if any.
     * @return a {@link FilePublicUrlResponse}
     */
    FilePublicUrlResponse getPublicUrl(String path, FileDownloadOption options);

    /**
     * <p>POST /object/list/{bucketName}</p>
     *
     * @param options Options for the file search
     * @return a list of {@link io.supabase.data.file.File}
     */
    CompletableFuture<List<io.supabase.data.file.File>> list(FileSearchOptions options);

    /**
     * <p>POST /object/list/{bucketName}</p>
     *
     * @param path    The prefix to list objects by
     * @param options Options for the file search
     * @return a list of {@link io.supabase.data.file.File}
     */
    CompletableFuture<List<io.supabase.data.file.File>> list(String path, FileSearchOptions options);

    /**
     * <p>DELETE /object/{bucketName}</p>
     *
     * @param paths a list of paths to files that should be deleted. BE WARY! PROVIDING NULL OR AN EMPTY LIST WILL DELETE THE ENTIRE BUCKET!
     * @return a list of the deleted files
     */
    CompletableFuture<List<io.supabase.data.file.File>> delete(List<String> paths);
}
