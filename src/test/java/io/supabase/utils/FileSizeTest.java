package io.supabase.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSizeTest {

    @Test
    public void fileSizeParsesCorrectly() {
        FileSize fileSizeInKb = new FileSize("1000B");
        FileSize fileSizeInGB = new FileSize("20GB");

        assertEquals("1KB", fileSizeInKb.getFileSizeAsKB());
        assertEquals("0MB", fileSizeInKb.getFileSizeAsMB());

        assertEquals("20000MB", fileSizeInGB.getFileSizeAsMB());
        assertEquals("20000000KB", fileSizeInGB.getFileSizeAsKB());
        assertEquals("20000000000B", fileSizeInGB.getFileSizeAsB());
    }
}
