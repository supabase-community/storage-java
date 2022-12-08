package io.supabase.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.supabase.data.file.FileDownload;
import io.supabase.errors.StorageException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RestUtils {
    private final static OkHttpClient CLIENT = new OkHttpClient();
    private final static Gson MAPPER = new GsonBuilder().setLenient().create();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static <T> CompletableFuture<T> get(TypeToken<T> typeToken, Map<String, String> headers, String url) {

        Request request = new Request.Builder()
            .url(url)
            .get()
            .headers(Headers.of(headers))
            .build();

        return getCompletableFuture(request, typeToken.getType());

    }

    public static <T> CompletableFuture<T> post(TypeToken<T> typeToken, Map<String, String> headers, String url, JsonObject body) {

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(MAPPER.toJson(body), JSON))
            .headers(Headers.of(headers))
            .build();

        return getCompletableFuture(request, typeToken.getType());

    }

    public static <T> CompletableFuture<T> put(TypeToken<T> typeToken, Map<String, String> headers, String url, JsonObject body) {

        Request request = new Request.Builder()
            .url(url)
            .put(RequestBody.create(MAPPER.toJson(body), JSON))
            .headers(Headers.of(headers))
            .build();

        return getCompletableFuture(request, typeToken.getType());

    }

    public static <T> CompletableFuture<T> delete(TypeToken<T> typeToken, Map<String, String> headers, String url, JsonObject body) {
        Request request = new Request.Builder()
            .url(url)
            .delete(RequestBody.create(MAPPER.toJson(body), JSON))
            .headers(Headers.of(headers))
            .build();

        return getCompletableFuture(request, typeToken.getType());
    }

    @NotNull
    public static <T> CompletableFuture<T> getCompletableFuture(Request request, Type type) {
        Call call = CLIENT.newCall(request);
        CompletableFuture<T> future = new CompletableFuture<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    JsonObject object = MAPPER.fromJson(response.body().string(), JsonObject.class);
                    future.completeExceptionally(new StorageException(object.get("error").getAsString(), object.get("statusCode").getAsInt(), object.get("message").getAsString()));
                    return;
                }
                future.complete(MAPPER.fromJson(response.body().string(), type));
            }
        });

        return future;
    }

    public static  CompletableFuture<FileDownload> getFile(Request request) {
        Call call = CLIENT.newCall(request);
        CompletableFuture<FileDownload> future = new CompletableFuture<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    JsonObject object = MAPPER.fromJson(response.body().string(), JsonObject.class);
                    future.completeExceptionally(new StorageException(object.get("error").getAsString(), object.get("statusCode").getAsInt(), object.get("message").getAsString()));
                    return;
                }
                future.complete(new FileDownload(response.body().bytes(), response.body().contentType()));
            }
        });

        return future;
    }
}
