package io.supabase.errors;

public class StorageException extends Exception {
    private final String statusCode;
    private final String error;

    /**
     * Constructs a new error with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public StorageException(String message, String statusCode, String error) {
        super(message);
        this.statusCode = statusCode;
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }
}
