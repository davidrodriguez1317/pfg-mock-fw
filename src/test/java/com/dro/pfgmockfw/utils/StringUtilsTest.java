package com.dro.pfgmockfw.utils;

import ch.qos.logback.core.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "http://www.example.com, www.example.com",
            "https://www.example.com, www.example.com",
            "www.example.com, www.example.com"
    })
    void givenUrl_whenStripHttpFromUrl_thenUrlWithoutHttpPrefixInLowercase(String url, String expected) {
        //given //when
        String result = StringUtils.stripHttpFromUrl(url);

        //then
        assertEquals(expected, result);
    }

    @Test
    void testRemoveFileExtension() {

        String fileName1 = "archivo.txt";
        String expected1 = "archivo";
        String result1 = StringUtils.removeFileExtension(fileName1);
        assertEquals(expected1, result1);

        String fileName2 = "documento.pdf";
        String expected2 = "documento";
        String result2 = StringUtils.removeFileExtension(fileName2);
        assertEquals(expected2, result2);

        String fileName3 = "imagen.jpg";
        String expected3 = "imagen";
        String result3 = StringUtils.removeFileExtension(fileName3);
        assertEquals(expected3, result3);

        String fileName4 = "no_extension";
        String expected4 = "no_extension";
        String result4 = StringUtils.removeFileExtension(fileName4);
        assertEquals(expected4, result4);
    }

    @Test
    public void testDecodeFromBase64() {
        //given
        String encodedString = "SGVsbG8gV29ybGQh";
        String expectedDecodedString = "Hello World!";

        //when
        String decodedString = StringUtils.decodeFromBase64(encodedString);

        //then
        assertEquals(expectedDecodedString, decodedString);
    }

    @Test
    public void testGetLastLines() {
        //given
        String input = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\n";
        int allowedLines = 3;
        String expectedLastLines = "Line 4\nLine 5\nLine 6";

        //when
        String lastLines = StringUtils.getLastLines(input, allowedLines);

        //then
        assertEquals(expectedLastLines, lastLines);
    }

    @Test
    public void testGetLastLinesWithNullInput() {
        //given
        String input = null;
        int allowedLines = 3;
        String expectedLastLines = "";

        //when
        String lastLines = StringUtils.getLastLines(input, allowedLines);

        //then
        assertEquals(expectedLastLines, lastLines);
    }

    @Test
    public void testGetLastLinesWithZeroAllowedLines() {
        //given
        String input = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\n";
        int allowedLines = 0;
        String expectedLastLines = "";

        //when
        String lastLines = StringUtils.getLastLines(input, allowedLines);

        //then
        assertEquals(expectedLastLines, lastLines);
    }
}
