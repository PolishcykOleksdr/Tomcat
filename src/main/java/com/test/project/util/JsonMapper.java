package com.test.project.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
    private JsonMapper() {
    }

    public static ObjectMapper create() {
        return new ObjectMapper();
    }
}
