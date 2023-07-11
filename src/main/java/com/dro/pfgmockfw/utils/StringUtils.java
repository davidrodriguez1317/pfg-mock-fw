package com.dro.pfgmockfw.utils;

import com.dro.pfgmockfw.exception.NoDataAvailableException;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {

    public static String stripHttpFromUrl(final String url) {
        String regex = "^(http[s]?://)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.replaceFirst("");
    }

    public String removeFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    public static String decodeFromBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static String getLastLines(final String input, final int allowedLines) {
        return input == null ? ""
                : Arrays.stream(input.split("\n"))
                .skip(Math.max(0, input.lines().count() - allowedLines))
                .collect(Collectors.joining("\n"));
    }

}
