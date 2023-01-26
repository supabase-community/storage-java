package io.supabase.data.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FileTransformOptionsTest {

    @Test
    public void correctValuesDoesNotThrow() {
        Assertions.assertDoesNotThrow(() -> new FileTransformOptions(500, 500, ResizeOption.COVER, 50, FormatOption.ORIGIN));
    }

    @Test
    public void incorrectWidthThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(0, 500, ResizeOption.CONTAIN, 50, FormatOption.NONE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(2501, 500, ResizeOption.CONTAIN, 50, FormatOption.NONE));
    }

    @Test
    public void incorrectHeightThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(500, 0, ResizeOption.CONTAIN, 50, FormatOption.NONE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(500, 2501, ResizeOption.CONTAIN, 50, FormatOption.NONE));
    }

    @Test
    public void nullResizeOptionGetsDefaulted() {
        Assertions.assertEquals(ResizeOption.COVER, new FileTransformOptions(500, 500, null, 50, FormatOption.NONE).getResizeOption());
    }

    @Test
    public void nullFormatOptionGetsDefaulted() {
        Assertions.assertEquals(FormatOption.NONE, new FileTransformOptions(500, 500, ResizeOption.CONTAIN, 50, null).getFormat());
    }

    @Test
    public void incorrectQualityThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(500, 500, ResizeOption.CONTAIN, -1, FormatOption.NONE));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileTransformOptions(500, 500, ResizeOption.CONTAIN, 101, FormatOption.NONE));
    }


}
