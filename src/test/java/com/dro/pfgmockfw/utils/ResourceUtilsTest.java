package com.dro.pfgmockfw.utils;

import com.dro.pfgmockfw.exception.NoDataAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceUtilsTest {

    @Test
    void givenExistingFilePath_whenGetStringFromResources_thenReturnStringContent() {
        //given
        String filePath = "testfile.txt";

        //when
        String result = ResourceUtils.getStringFromResources(filePath);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.contains("This is a test file."));
    }

    @Test
    void givenNonExistingFilePath_whenGetStringFromResources_thenThrowException() {
        //given
        String filePath = "nonexistent.txt";

        //when //then
        assertThrows(NoDataAvailableException.class, () -> {
            ResourceUtils.getStringFromResources(filePath);
        });
    }

    @Test
    void testListFilesFromDirectory() {
        //given //when
        List<String> fileList = ResourceUtils.listFilesFromDirectory("responses");

        // then
        Assertions.assertTrue(fileList.size() > 0);
    }

    @Test
    void testListFilesFromDirectory_whenEmpty() {
        //given //when //then
        assertThrows(NoDataAvailableException.class, () -> {
            ResourceUtils.listFilesFromDirectory("no_responses");
        });
    }
}
