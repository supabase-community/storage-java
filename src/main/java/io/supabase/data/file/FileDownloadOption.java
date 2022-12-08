package io.supabase.data.file;

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
}
