package io.supabase.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.supabase.data.file.*;
import io.supabase.utils.HttpMethod;
import io.supabase.utils.MessageResponse;
import io.supabase.utils.RequestBodyUtil;
import io.supabase.utils.RestUtils;
import okhttp3.*;

import java.io.*;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        InputStream fileStream = null;
        RequestBody requestBody = null;
        try {
            fileStream = new FileInputStream(file);
            requestBody = RequestBodyUtil.create(MediaType.parse(Files.probeContentType(Paths.get(file.getPath()))), fileStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String finalPath = getFinalPath(path);
        Request request = new Request.Builder().url(this.url + "object/" + finalPath).headers(Headers.of(this.headers)).method(method.getValue(), requestBody).build();
        return RestUtils.getCompletableFuture(request, new TypeToken<FilePathResponse>(){}.getType());
    }

    @Override
    public CompletableFuture<FilePathResponse> upload(String path, File file) {
        return uploadOrUpdate(HttpMethod.POST, path, file);
    }

    @Override
    public CompletableFuture<FilePathResponse> update(String path, File file) {
        return uploadOrUpdate(HttpMethod.PUT, path, file);
    }

    @Override
    public CompletableFuture<MessageResponse> move(String fromPath, String toPath) {
        JsonObject body = new JsonObject();
        body.addProperty("bucketId", this.bucketId);
        body.addProperty("sourceKey", fromPath);
        body.addProperty("destinationKey", toPath);
        return RestUtils.post(new TypeToken<MessageResponse>(){}, headers, this.url + "object/move", body);
    }

    @Override
    public CompletableFuture<FilePathResponse> copy(String fromPath, String toPath) {
        JsonObject body = new JsonObject();
        body.addProperty("bucketId", this.bucketId);
        body.addProperty("sourceKey", fromPath);
        body.addProperty("destinationKey", toPath);
        return RestUtils.post(new TypeToken<FilePathResponse>(){}, headers, this.url + "object/copy", body);
    }

    @Override
    public CompletableFuture<FileSignedUrlResponse> getSignedUrl(String path, int expiresIn, FileDownloadOption options) {
        JsonObject body = new JsonObject();
        body.addProperty("expiresIn", expiresIn);
        String downloadParams = "";
        if (options.isDownload()) {
            downloadParams += "&download=";
            if (options.getDownloadName() != null) {
                downloadParams += options.getDownloadName();
            } else {
                downloadParams += "true";
            }
        }

        CompletableFuture<FileSignedUrlResponse> signedUrlFuture = RestUtils.post(new TypeToken<FileSignedUrlResponse>(){}, headers, this.url + "object/sign/" + getFinalPath(path), body);

        String finalDownloadParams = downloadParams;
        return signedUrlFuture.thenApply(fileSignedUrlResponse -> new FileSignedUrlResponse(this.url + fileSignedUrlResponse.getSignedUrl() + finalDownloadParams));
    }

    @Override
    public CompletableFuture<List<FileSignedUrlResponse>> getSignedUrls(List<String> paths, int expiresIn, FileDownloadOption options) {
        JsonObject body = new JsonObject();
        JsonArray bodyPaths = new JsonArray();
        for (String path : paths) {
            bodyPaths.add(path);
        }
        body.addProperty("expiresIn", expiresIn);
        body.add("paths", bodyPaths);
        String downloadParams = "";
        if (options.isDownload()) {
            downloadParams += "&download=";
            if (options.getDownloadName() != null) {
                downloadParams += options.getDownloadName();
            } else {
                downloadParams += "true";
            }
        }

        CompletableFuture<List<FileSignedUrlResponse>> signedUrlFuture = RestUtils.post(new TypeToken<List<FileSignedUrlResponse>>(){}, headers, this.url + "object/sign/" + bucketId, body);

        String finalDownloadParams = downloadParams;
        return signedUrlFuture.thenApply(fileSignedUrlResponse -> fileSignedUrlResponse.stream().map((fileSignedUrlResponse1 -> new FileSignedUrlResponse(this.url + fileSignedUrlResponse1.getSignedUrl() + finalDownloadParams))).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<FileDownload> download(String path) {
        Request request = new Request.Builder()
            .url(url + "object/" + getFinalPath(path))
            .get()
            .headers(Headers.of(headers))
            .build();

        return RestUtils.getFile(request);
    }

    @Override
    public FilePublicUrlResponse getPublicUrl(String path, FileDownloadOption options) {
        String downloadParams = "";
        if (options.isDownload()) {
            downloadParams += "&download=";
            if (options.getDownloadName() != null) {
                downloadParams += options.getDownloadName();
            } else {
                downloadParams += "true";
            }
        }
        return new FilePublicUrlResponse(this.url + "object/public/" + getFinalPath(path) + downloadParams);
    }

    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> list(FileSearchOptions options) {
        return list("", options);
    }

    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> list(String path, FileSearchOptions options) {
        JsonObject body = new Gson().toJsonTree(options).getAsJsonObject();
        body.addProperty("prefix", path);
        body.addProperty("search", "");
        return RestUtils.post(new TypeToken<List<io.supabase.data.file.File>>(){}, headers, this.url + "object/list/" + this.bucketId, body);
    }

    @Override
    public CompletableFuture<List<io.supabase.data.file.File>> delete(List<String> paths) {
        JsonObject body = new JsonObject();
        JsonArray prefixes = new JsonArray();
        for (String path : paths) {
            prefixes.add(path);
        }
        body.add("prefixes", prefixes);
        return RestUtils.delete(new TypeToken<List<io.supabase.data.file.File>>(){}, headers, this.url + "object/" + bucketId, body);
    }

    private String getFinalPath(String path) {
        return bucketId + "/" + path;
    }
}
