package com.dro.pfgmockfw.utils;

import com.dro.pfgmockfw.exception.NoDataAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {

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
        Assertions.assertThrows(NoDataAvailableException.class, () -> {
            ResourceUtils.getStringFromResources(filePath);
        });
    }

}
