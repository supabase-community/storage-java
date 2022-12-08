package io.supabase.data.file;

import okhttp3.MediaType;

public class FileDownload {
    private byte[] bytes;
    private MediaType type;

    public FileDownload(byte[] bytes, MediaType type) {
        this.bytes = bytes;
        this.type = type;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public MediaType getType() {
        return type;
    }
}
