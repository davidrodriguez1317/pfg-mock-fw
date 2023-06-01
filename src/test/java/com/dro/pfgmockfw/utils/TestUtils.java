package com.dro.pfgmockfw.utils;

import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
    public static String concatenateTags(List<String> tags) {
        String concatenatedTags = tags.stream()
                .map(tag -> "\"" + tag + "\"")
                .collect(Collectors.joining(",", "[", "]"));

        return concatenatedTags;
    }
}
