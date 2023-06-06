package com.dro.pfgmockfw.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        Assertions.assertEquals(expected, result);
    }
}
