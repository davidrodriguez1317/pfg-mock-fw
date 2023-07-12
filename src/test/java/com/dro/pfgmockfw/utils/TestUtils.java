package com.dro.pfgmockfw.utils;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {
    public static String joinWithCommasInsideQuotes(final List<String> tags) {

        return tags.stream()
                .map(tag -> "\"" + tag + "\"")
                .collect(Collectors.joining(","));
    }

    public static String normalizeLineEndings(String input) {
        return input.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    }
}
