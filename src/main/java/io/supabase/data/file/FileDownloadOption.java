package io.supabase.data.file;

import java.util.HashMap;
import java.util.Map;

public class FileDownloadOption {
    private String downloadName;
    private final boolean download;

    public FileDownloadOption(String downloadName) {
        this.downloadName = downloadName;
        this.download = true;
    }

    public FileDownloadOption(boolean download) {
        this.download = download;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public boolean isDownload() {
        return download;
    }

    public Map<String, String> convertToMap() {
        Map<String, String> map = new HashMap<>();
        if (this.isDownload()) map.put("download", this.getDownloadName() != null ? this.getDownloadName() : "true");

        return map;
    }
}
