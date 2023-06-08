package com.dro.pfgmockfw.utils;

import com.dro.pfgmockfw.exception.NoDataAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testListFilesFromDirectory() {
        //given
        String tempFolderPath = System.getProperty("java.io.tmpdir");
        String testFolderName = tempFolderPath + "testFolder";
        File testFolder = new File(testFolderName);
        testFolder.mkdir();

        try {
            File testFile1 = new File(testFolder, "file1.txt");
            testFile1.createNewFile();

            File testFile2 = new File(testFolder, "file2.txt");
            testFile2.createNewFile();

            File testFile3 = new File(testFolder, "file3.txt");
            testFile3.createNewFile();

            //when
            List<String> fileList = ResourceUtils.listFilesFromDirectory(testFolderName);

            // then
            Assertions.assertEquals(3, fileList.size());
            Assertions.assertTrue(fileList.contains("file1.txt"));
            Assertions.assertTrue(fileList.contains("file2.txt"));
            Assertions.assertTrue(fileList.contains("file3.txt"));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("There was an error checking files in folder");
        } finally {
            File[] testFiles = testFolder.listFiles();
            if (testFiles != null) {
                Arrays.stream(testFiles).forEach(File::delete);
            }
            testFolder.delete();
        }
    }

    @Test
    public void testListFilesFromDirectory_whenEmpty() {
        //given
        String tempFolderPath = System.getProperty("java.io.tmpdir");
        String testFolderName = tempFolderPath + "testFolder";
        File testFolder = new File(testFolderName);
        testFolder.mkdir();

        try {
            //when
            List<String> fileList = ResourceUtils.listFilesFromDirectory(testFolderName);

            // then
            Assertions.assertEquals(0, fileList.size());

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("There was an error checking files in folder");
        } finally {
            testFolder.delete();
        }
    }

}
