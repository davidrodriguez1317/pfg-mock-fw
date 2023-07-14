package com.dro.pfgmockfw.utils;

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
    public void testGetServiceNameFromFileName() {

        String fileName1 = "pfg-product-cost-0.0.3-SNAPSHOT.jar";
        String resultado1 = StringUtils.getServiceNameFromFileName(fileName1);
        assertEquals("pfg-product-cost", resultado1);

        String fileName2 = "pfg-some-1.0.3-SNAPSHOT.jar";
        String resultado2 = StringUtils.getServiceNameFromFileName(fileName2);
        assertEquals("pfg-some", resultado2);

        String fileName3 = "product-1.0.3-SNAPSHOT.jar";
        String resultado3 = StringUtils.getServiceNameFromFileName(fileName3);
        assertEquals("product", resultado3);
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

}
