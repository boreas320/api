package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by xiangshuai on 2017/12/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andDo(document("index", responseFields(fieldWithPath("hello").type(JsonFieldType.STRING).description("哈哈哈"))));
    }

    @Test
    public void getNotes() throws Exception {
        ConstrainedFields getNotesInputFields = new ConstrainedFields(GetNotesInput.class);
        GetNotesInput getNotesInput = new GetNotesInput();
        getNotesInput.setSize(9);

        mockMvc.perform(get("/getNotes").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(getNotesInput))).
                andExpect(status().isOk()).
                andDo(document("index",
                        requestFields(getNotesInputFields.withPath("size").description("列表长度")),
                        responseFields(
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("日报"),
                                fieldWithPath("[].cts").type(JsonFieldType.NUMBER).description("创建日期"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("内容"),
                                fieldWithPath("[].author.name").type(JsonFieldType.STRING).description("作者姓名")
                        )
                ));
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}