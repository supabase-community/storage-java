package io.supabase.api;

import io.supabase.data.file.*;
import io.supabase.utils.MessageResponse;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IStorageFileAPI {

    CompletableFuture<FilePathResponse> upload(String path, File file);
    CompletableFuture<FilePathResponse> update(String path, File file);
    CompletableFuture<MessageResponse> move(String fromPath, String toPath);
    CompletableFuture<FilePathResponse> copy(String fromPath, String toPath);
    CompletableFuture<FileSignedUrlResponse> getSignedUrl(String path, int expiresIn, FileDownloadOption options);
    CompletableFuture<List<FileSignedUrlResponse>> getSignedUrls(List<String> paths, int expiresIn, FileDownloadOption options);
    CompletableFuture<FileDownload> download(String path);
    FilePublicUrlResponse getPublicUrl(String path, FileDownloadOption options);

    CompletableFuture<List<io.supabase.data.file.File>> list(FileSearchOptions options);
    CompletableFuture<List<io.supabase.data.file.File>> list(String path, FileSearchOptions options);
    CompletableFuture<List<io.supabase.data.file.File>> delete(List<String> paths);
}
