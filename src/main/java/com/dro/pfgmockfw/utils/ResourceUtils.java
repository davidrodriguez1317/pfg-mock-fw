package com.dro.pfgmockfw.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import com.dro.pfgmockfw.exception.NoDataAvailableException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {

    public static String getStringFromResources(final String filePath) {
        InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(filePath);

        if(Objects.isNull(inputStream)) {
            throw new NoDataAvailableException("No data found for path: ". concat(filePath));
        }

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
