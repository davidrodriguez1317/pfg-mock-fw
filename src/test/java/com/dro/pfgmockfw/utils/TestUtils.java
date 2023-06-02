package com.dro.pfgmockfw.utils;

import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
    public static String joinWithCommasInsideQuotes(final List<String> tags) {

        return tags.stream()
                .map(tag -> "\"" + tag + "\"")
                .collect(Collectors.joining(","));
    }
}
