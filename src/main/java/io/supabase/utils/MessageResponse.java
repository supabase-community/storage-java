package io.supabase.utils;

public class MessageResponse {
    private final String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
