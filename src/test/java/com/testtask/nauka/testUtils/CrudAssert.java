package com.testtask.nauka.testUtils;

import com.jayway.jsonpath.JsonPath;
import com.testtask.nauka.types.CheckedConsumer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.joinPrefixToPath;
import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CrudAssert<T> {
    private final MockMvc mockMvc;
    private final CrudRepository<T, Long> crudRepository;
    private final String basePath;
    private final Map<String, String> queryArgs = new HashMap<>();

    public CrudAssert(MockMvc mvc, CrudRepository<T, Long> repository, String basePath) {
        this.mockMvc = mvc;
        this.crudRepository = repository;
        this.basePath = basePath;
    }

    /**
     * Assert GET request to <b><i>getById</i></b> endpoint with matchers:
     * {@code status().isOk()} and {@code jsonPathCrud("$", hasSize(listSize))}
     * @param listSize size of the array in json output
     */
    public void assertGetAll(int listSize) throws Exception {
        assertGetAll(List.of(
                status().isOk(),
                jsonPathCrud("$", hasSize(listSize))
        ));
    }

    /**
     * Assert GET request to <b><i>getAll</i></b> endpoint with input matchers
     * @param matchers list of matchers
     */
    public void assertGetAll(List<ResultMatcher> matchers) throws Exception {
        mockMvc.perform(get(createPath()))
                .andExpect(matchAll(matchers.toArray(ResultMatcher[]::new)));
    }

    /**
     * Assert GET request to <b><i>getById</i></b> endpoint with matchers:
     * {@code status().isOk()} and {@code jsonPathCrud("$").isNotEmpty()}
     * @param id id added to the path
     */
    public void assertGetById(Long id) throws Exception {
        assertGetById(id, List.of(
                status().isOk(),
                jsonPathCrud("$").isNotEmpty()
        ));
    }

    /**
     * Assert GET request to <b><i>getById</i></b> endpoint with input matchers
     * @param id id added to the path
     * @param matchers list of matchers
     */
    public void assertGetById(Long id, List<ResultMatcher> matchers) throws Exception {
        mockMvc.perform(get(createPath(id)))
                .andExpect(matchAll(matchers.toArray(ResultMatcher[]::new)));
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with matchers:
     * {@code status().isCreated()} and {@code jsonPath("$").isNotEmpty()}
     * If request returned 201 status code checks that repository got one more entity.
     * @param json json string sent to endpoint
     */
    public void assertCreate(String json) throws Exception {
        assertCreate(json, null, List.of(
                status().isCreated(),
                jsonPathCrud("$").isNotEmpty()
        ), t -> {});
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with input matchers.
     * If request returned 201 status code checks that repository got one more entity.
     * @param json json string sent to endpoint
     * @param matchers list of matchers
     */
    public void assertCreate(String json, List<ResultMatcher> matchers) throws Exception {
        assertCreate(json, null, matchers, t -> {});
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with matchers:
     * {@code status().isCreated()} and {@code jsonPathCrud("$").isNotEmpty()},
     * and checks that entity with id (found by supplied jsonPathId matcher) from json was created.
     * @param json json string sent to endpoint
     * @param jsonPathId JsonPath style path to id in output json
     */
    public void assertCreate(String json, String jsonPathId) throws Exception {
        assertCreate(json, jsonPathId, List.of(
                status().isCreated(),
                jsonPathCrud("$").isNotEmpty()
        ), t -> {});
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with matchers:
     * {@code status().isCreated()} and {@code jsonPathCrud("$").isNotEmpty()},
     * checks that entity was created in the repository and asserts created entity with input assert function.
     * @param json json string sent to endpoint
     * @param jsonPathId JsonPath style path to id in output json
     * @param assertFunc lambda function that gets created entity
     */
    public void assertCreate(String json, String jsonPathId, CheckedConsumer<T> assertFunc) throws Exception {
        assertCreate(json, jsonPathId, List.of(
                status().isCreated(),
                jsonPathCrud("$").isNotEmpty()
        ),  assertFunc);
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with input matchers.
     * If request returned 201 status code checks that entity was created in the repository
     * @param json json string sent to endpoint
     * @param matchers list of matchers
     * @param jsonPathId JsonPath style path to id in output json
     */
    public void assertCreate(String json, String jsonPathId, List<ResultMatcher> matchers) throws Exception {
        assertCreate(json, jsonPathId, matchers,  t -> {});
    }

    /**
     * Assert POST request to <b><i>create</i></b> endpoint with input matchers.
     * If request returned 201 status code checks that entity was created in the repository
     * and asserts the entity with input assert function.
     * @param json json string sent to endpoint
     * @param matchers list of matchers
     * @param jsonPathId JsonPath style path to id in output json
     * @param assertFunc lambda function that gets created entity
     */
    public void assertCreate(String json, String jsonPathId, List<ResultMatcher> matchers, CheckedConsumer<T> assertFunc)
            throws Exception
    {
        long countBefore = crudRepository.count();

        MvcResult result = mockMvc.perform(post(basePath)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(matchAll(matchers.toArray(ResultMatcher[]::new)))
                .andReturn();

        if (result.getResponse().getStatus() != HttpServletResponse.SC_CREATED) {
            return;
        }

        assertThat(crudRepository.count()).isEqualTo(countBefore + 1);

        if (jsonPathId == null) {
            return;
        }
        String resultJson = result.getResponse().getContentAsString();
        Integer savedId = JsonPath.read(resultJson, joinPrefixToPath(jsonPathId));
        assertThat(crudRepository.findById(savedId.longValue())).hasValueSatisfying(assertFunc);
    }

    /**
     * Assert PUT request to <b><i>update</i></b> endpoint with matchers:
     * {@code status().isCreated()} and {@code jsonPathCrud("$").isNotEmpty()}
     * If request returned 200 status code, check that updated entity exists.
     * @param id id added to the path
     * @param json json string sent to endpoint
     */
    public void assertUpdate(Long id, String json) throws Exception {
        assertUpdate(id, json, List.of(
                status().isOk(),
                jsonPathCrud("$").isNotEmpty()
        ), t -> {});
    }

    /**
     * Assert PUT request to <b><i>update</i></b> endpoint with input matchers.
     * If request returned 200 status code, check that updated entity exists.
     * @param id id added to the path
     * @param json json string sent to endpoint
     * @param matchers list of matchers
     */
    public void assertUpdate(Long id, String json, List<ResultMatcher> matchers) throws Exception {
        assertUpdate(id, json, matchers, t -> {});
    }

    /**
     * Assert PUT request to <b><i>update</i></b> endpoint with input matchers.
     * If request returned 200 status code, assert updated entity with input assert function.
     * @param id id added to the path
     * @param json json string sent to endpoint
     * @param matchers list of matchers
     * @param assertFunc lambda function that gets created entity
     */
    public void assertUpdate(Long id, String json, List<ResultMatcher> matchers, CheckedConsumer<T> assertFunc)
            throws Exception {
        MvcResult result = mockMvc.perform(put(createPath(id))
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(matchAll(matchers.toArray(ResultMatcher[]::new)))
                .andReturn();

        if (result.getResponse().getStatus() != HttpServletResponse.SC_OK) {
            return;
        }

        assertThat(crudRepository.findById(id)).hasValueSatisfying(assertFunc);
    }

    /**
     * Assert DELETE request to <b><i>delete</i></b> endpoint with matcher: {@code status().isNoContent()}
     * If request returned 204 status code check that entity was deleted.
     * @param id id added to the path
     */
    public void assertDelete(Long id) throws Exception {
        assertDelete(id, List.of(
                status().isNoContent()
        ));
    }

    /**
     * Assert DELETE request to <b><i>delete</i></b> endpoint with input matchers.
     * If request returned 204 status code, check that entity was deleted.
     * @param id id added to the path
     * @param matchers list of matchers
     */
    public void assertDelete(Long id, List<ResultMatcher> matchers) throws Exception {
        long countBefore = crudRepository.count();
        MvcResult result = mockMvc.perform(delete(createPath(id)))
                .andExpect(matchAll(matchers.toArray(ResultMatcher[]::new)))
                .andReturn();

        if (result.getResponse().getStatus() != HttpServletResponse.SC_NO_CONTENT) {
            return;
        }

        assertThat(crudRepository.count()).isEqualTo(countBefore - 1);
        assertThat(crudRepository.findById(id)).isEmpty();
    }

    /**
     * Reset class instance
     */
    public void reset() {
        resetQueryArgs();
    }

    /**
     * Add path query key-value pair
     * @param key pair key
     * @param value pair value
     */
    public void addQueryArg(String key, Object value) {
        queryArgs.put(key, value.toString());
    }

    /**
     * Reset path query
     */
    public void resetQueryArgs() {
        queryArgs.clear();
    }


    private String createPath() {
        return createPath(null);
    }

    private String createPath(Long id) {
        StringBuilder builder = new StringBuilder(basePath);
        if (id != null) {
            builder.append("/").append(id);
        }
        if (!queryArgs.isEmpty()) {
            builder.append("?");
            builder.append(queryArgs.entrySet().stream()
                    .map(e -> e.getKey()+"="+e.getValue())
                    .collect(joining("&")));
        }
        return builder.toString();
    }
}
