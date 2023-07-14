package com.dro.pfgmockfw.utils;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {

    public static String stripHttpFromUrl(final String url) {
        String regex = "^(http[s]?://)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.replaceFirst("");
    }

    public String getServiceNameFromFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return removeVersion(fileName.substring(0, dotIndex));
        }
        return removeVersion(fileName);
    }

    public static String removeVersion(String cadena) {
        String regex = "([a-zA-Z-]+)(-\\d+\\.\\d+\\.\\d+)?(-SNAPSHOT)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cadena);
        if (matcher.matches()) {
            return  matcher.group(1);
        }
        return Strings.EMPTY;
    }

    public static String decodeFromBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


}
