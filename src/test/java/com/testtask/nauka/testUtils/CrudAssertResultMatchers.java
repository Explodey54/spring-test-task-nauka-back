package com.testtask.nauka.testUtils;

import org.hamcrest.Matcher;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CrudAssertResultMatchers {
    private static final String JSON_PATH_PREFIX = "$.data";

    public static JsonPathResultMatchers jsonPathCrud(String path) {
        return jsonPath(joinPrefixToPath(path));
    }

    public static ResultMatcher jsonPathCrud(String path, Matcher matcher) {
        return jsonPath(joinPrefixToPath(path), matcher);
    }

    public static String joinPrefixToPath(String path) {
        return JSON_PATH_PREFIX + path.replace("$", "");
    }
}
