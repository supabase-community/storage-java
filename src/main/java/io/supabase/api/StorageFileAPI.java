package io.supabase.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.supabase.data.file.*;
import io.supabase.utils.HttpMethod;
import io.supabase.utils.MessageResponse;
import io.supabase.utils.RestUtils;
import io.supabase.utils.Utilities;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StorageFileAPI implements IStorageFileAPI {
    private final String url;
    private final Map<String, String> headers;
    private final String bucketId;

    public StorageFileAPI(String url, Map<String, String> headers, String bucketId) {
        this.url = url;
        this.headers = headers;
        this.bucketId = bucketId;
    }

    private CompletableFuture<FilePathResponse> uploadOrUpdate(HttpMethod method, String path, File file) {
        InputStream fileStream;
        RequestBody requestBody;
        try {
            fileStream = Files.newInputStream(file.toPath());
            requestBody = Utilities.createRequestBody(MediaType.parse(Files.probeContentType(Paths.get(file.getPath()))), fileStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String finalPath = getFinalPath(path);
        Request request = new Request.Builder().url(this.url + "object/" + finalPath).headers(Headers.of(this.headers)).method(method.getValue(), requestBody).build();
        return RestUtils.getCompletableFuture(request, new TypeToken<FilePathResponse>() {
        }.getType());
    }

    /**
     * <p>POST /object/{bucketName}/{wildcard}</p>
     * @param path The path to the file within the bucket of where it should be uploaded.
     * @param file The file that needs to be uploaded.
     * @return a {@link FilePathResponse}
     */
    @Override
    public CompletableFuture<FilePathResponse> upload(String path, File file) {
        return uploadOrUpdate(HttpMethod.POST, path, file);
    }

    /**
     * <p>PUT /object/{bucketName}/{wildcard}</p>
     * @param path The path to the file within the bucket that should get updated.
     * @param file The new file that should be placed.
     * @return a {@link FilePathResponse}
     */
    @Override
    public CompletableFuture<FilePathResponse> update(String path, File file) {
        return uploadOrUpdate(HttpMethod.PUT, path, file);
    }

    /**
     * <p>POST /object/move/</p>
     * @param fromPath The path to the object that needs to be moved.
     * @param toPath The new path where the object should be moved to.
     * @return a {@link MessageResponse}
     */
    @Override
    public CompletableFuture<MessageResponse> move(String fromPath, String toPath) {
        JsonObject body = new JsonObject();
        body.addProperty("bucketId", this.bucketId);
        body.addProperty("sourceKey", fromPath);
        body.addProperty("destinationKey", toPath);
        return RestUtils.post(new TypeToken<MessageResponse>() {
        }, headers, this.url + "object/move", body);
    }

    /**
     * <p>POST /object/copy/</p>
     * @param fromPath The path to the object that needs to be copied.
     * @param toPath The new path where the object should be copied to.
     * @return a {@link FilePathResponse}
     */
    @Override
    public CompletableFuture<FilePathResponse> copy(String fromPath, String toPath) {
        JsonObject body = new JsonObject();
        body.addProperty("bucketId", this.bucketId);
        body.addProperty("sourceKey", fromPath);
        body.addProperty("destinationKey", toPath);
        return RestUtils.post(new TypeToken<FilePathResponse>() {
        }, headers, this.url + "object/copy", body);
    }

    /**
     * <p>POST /object/sign/{bucketName}</p>
     * <p>This method just wraps {@link #getSignedUrls(List, int, FileDownloadOption, FileTransformOptions)}</p>
     *
     * @param path      The singular file path that should be signed.
     * @param expiresIn how many seconds until the signed url expires.
     * @param downloadOptions   any additional download options.
     * @param transformOptions The transform options if any
     * @return a {@link FileSignedUrlResponse}
     */
    @Override
    public CompletableFuture<FileSignedUrlResponse> getSignedUrl(String path, int expiresIn, FileDownloadOption downloadOptions, FileTransformOptions transformOptions) {
        CompletableFuture<List<FileSignedUrlResponse>> signedUrlFuture = getSignedUrls(List.of(path), expiresIn, downloadOptions, transformOptions);
        return signedUrlFuture.thenApply((responses) -> responses.get(0));
    }

    /**
     * <p>POST /object/sign/{bucketName}</p>
     *
     * @param paths     a list of file paths that should be signed.
     * @param expiresIn how many seconds until the signed urls expires.
     * @param downloadOptions   any additional download options.
     * @param transformOptions The transform options if any
     * @return a list of {@link FileSignedUrlResponse}
     */
    @Override
    public CompletableFuture<List<FileSignedUrlResponse>> getSignedUrls(List<String> paths, int expiresIn, FileDownloadOption downloadOptions, FileTransformOptions transformOptions) {
        JsonObject body = new JsonObject();
        JsonArray bodyPaths = new JsonArray();
        for (String path : paths) {
            bodyPaths.add(path);
        }
        body.addProperty("expiresIn", expiresIn);
        body.add("paths", bodyPaths);

        Map<String, String> downloadParamsMap = downloadOptions != null ? downloadOptions.convertToMap() : new HashMap<>();
        Map<String, String> transformParamsMap = transformOptions != null ? transformOptions.convertToMap() : new HashMap<>();
        Map<String, String> paramsMap = Utilities.combineMaps(downloadParamsMap, transformParamsMap);
        CompletableFuture<List<FileSignedUrlResponse>> signedUrlFuture = RestUtils.post(new TypeToken<List<FileSignedUrlResponse>>() {
        }, headers, this.url + "object/sign/" + bucketId, body);

        return signedUrlFuture.thenApply(fileSignedUrlResponse -> fileSignedUrlResponse.stream().map((fileSignedUrlResponse1 -> new FileSignedUrlResponse(this.url + fileSignedUrlResponse1.getSignedUrl() + Utilities.convertMapToQueryParams(paramsMap)))).collect(Collectors.toList()));
    }

    /**
     * <p>Downloads a file from a private bucket. To download something from a public bucket, make a request to the url from {@link #getPublicUrl(String, FileDownloadOption, FileTransformOptions)}</p>
     * <p>GET /object/authenticated/{bucketName}/{wildcard}</p>
     *
     * @param path The path of the file to download
     * @param transformOptions The transform options if any
     * @return a {@link FileDownload}
     */
    @Override
    public CompletableFuture<FileDownload> download(String path, FileTransformOptions transformOptions) {
        Request request = new Request.Builder().url(url + "object/authenticated/" + getFinalPath(path) + Utilities.convertMapToQueryParams(transformOptions.convertToMap())).get().headers(Headers.of(headers)).build();

        return RestUtils.getFile(request);
    }

    /**
     * Creates the url for an object in a public bucket
     *
     * @param path    The path of the file the link should point to.
     * @param downloadOptions The download options if any.
     * @param transformOptions The transform options if any
     * @return a {@link FilePublicUrlResponse}
     */
    @Override
    public FilePublicUrlResponse getPublicUrl(String path, FileDownloadOption downloadOptions, FileTransformOptions transformOptions) {
        boolean wantsTransformation = transformOptions != null;
        String urlSuffix = wantsTransformation ? "render/image" : "object";

        Map<String, String> transformParamsMap = transformOptions != null ? transformOptions.convertToMap() : new HashMap<>();
        Map<String, String> downloadParamsMap = downloadOptions != null ? downloadOptions.convertToMap() : new HashMap<>();
        Map<String, String> combined = Utilities.combineMaps(downloadParamsMap, transformParamsMap);

        return new FilePublicUrlResponse(this.url + urlSuffix + "/public/" + getFinalPath(path) + Utilities.convertMapToQueryParams(combined));
    }

    /**
     * <p>POST /object/list/{bucketName}</p>
     * @param options Options for the file search
     * @return a list of {@link io.supabase.data.file.File}
     */
    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> list(FileSearchOptions options) {
        return list("", options);
    }

    /**
     * <p>POST /object/list/{bucketName}</p>
     * @param path The prefix to list objects by
     * @param options Options for the file search
     * @return a list of {@link io.supabase.data.file.File}
     */
    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> list(String path, FileSearchOptions options) {
        JsonObject body = new Gson().toJsonTree(options).getAsJsonObject();
        body.addProperty("prefix", path);
        body.addProperty("search", "");
        return RestUtils.post(new TypeToken<List<io.supabase.data.file.File>>() {
        }, headers, this.url + "object/list/" + this.bucketId, body);
    }

    /**
     * <p>DELETE /object/{bucketName}</p>
     * @param paths a list of paths to files that should be deleted. BE WARY! PROVIDING NULL OR AN EMPTY LIST WILL DELETE THE ENTIRE BUCKET!
     * @return a list of the deleted files
     */
    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> delete(List<String> paths) {
        JsonObject body = new JsonObject();
        JsonArray prefixes = new JsonArray();
        for (String path : paths) {
            prefixes.add(path);
        }
        body.add("prefixes", prefixes);
        return RestUtils.delete(new TypeToken<List<io.supabase.data.file.File>>() {
        }, headers, this.url + "object/" + bucketId, body);
    }

    private String getFinalPath(String path) {
        return bucketId + "/" + path;
    }
}
