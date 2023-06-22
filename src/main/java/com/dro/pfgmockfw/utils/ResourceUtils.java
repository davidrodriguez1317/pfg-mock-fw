package com.dro.pfgmockfw.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import com.dro.pfgmockfw.exception.NoDataAvailableException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
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

    public static List<String> listFilesFromDirectory(final String folderName) {

        File folder = getFolderReference(folderName);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new NoDataAvailableException("It was not possible list files in folder ".concat(folderName));
        }

        File[] files = folder.listFiles();

        return Optional.ofNullable(files)
                .map(fs -> Arrays.stream(fs)
                        .map(File::getName)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public static File getFolderReference(final String folderName) {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        URL resource = classLoader.getResource(folderName);

        if (Objects.isNull(resource)) {
            throw new NoDataAvailableException("It was not possible list files in folder " + folderName);
        }

        return new File(resource.getFile());
    }
}
