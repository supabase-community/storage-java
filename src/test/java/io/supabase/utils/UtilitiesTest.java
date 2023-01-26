package io.supabase.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UtilitiesTest {

    @Test
    public void convertMapToQueryParamsWithValidInput() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        map.put("lorem", "ipsum");

        Assertions.assertEquals("?lorem=ipsum&hello=world", Utilities.convertMapToQueryParams(map));
    }

    @Test
    public void convertMapToQueryParamsWithEmptyMap() {
        Assertions.assertEquals("", Utilities.convertMapToQueryParams(new HashMap<>()));
    }

    @Test
    public void convertMapToQueryParamsWithOneParam() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");

        Assertions.assertEquals("?hello=world", Utilities.convertMapToQueryParams(map));
    }
}
