package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.http.HttpRequestSnippet;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by xiangshuai on 2017/12/29.
 */
@RunWith(SpringRunner.class)//获取spring环境,注入各种bean
/**
 * //支持spring boot,
 * Annotation that can be specified on a test class that runs Spring Boot based tests. Provides the following features over and above the regular Spring TestContext Framework:
 * > Uses SpringBootContextLoader as the default ContextLoader when no specific @ContextConfiguration(loader=...) is defined.
 * > Automatically searches for a @SpringBootConfiguration when nested @Configuration is not used, and no explicit classes are specified.
 * > Allows custom Environment properties to be defined using the properties attribute.
 * > Provides support for different webEnvironment modes, including the ability to start a fully running container listening on a defined or random port.
 * > Registers a TestRestTemplate bean for use in web tests that are using a fully running container.
 */
@SpringBootTest
public class IndexControllerTest {

//    @Autowired
//    private PrettyPrintHttpRequestSnippet prettyPrintHttpRequestSnippet;
//    @Autowired
//    private PrettyPrintHttpResponseSnippet prettyPrintHttpResponseSnippet;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;


    private MockMvc mockMvc;


    @Rule//类似于Before注解
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setUp() {
        DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.context);
        MockMvcRestDocumentationConfigurer mockMvcConfigurer = MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation);
        mockMvcConfigurer.snippets().withDefaults(HttpDocumentation.httpRequest(), HttpDocumentation.httpResponse());
        mockMvcConfigurer.operationPreprocessors().withRequestDefaults(Preprocessors.prettyPrint()).withResponseDefaults(Preprocessors.prettyPrint());
        mockMvcBuilder = mockMvcBuilder.apply(mockMvcConfigurer);
        this.mockMvc = mockMvcBuilder.build();
    }

    @Test
    public void getNotes() throws Exception {
        ConstrainedFields getNotesInputFields = new ConstrainedFields(GetNotesInput.class);
        GetNotesInput getNotesInput = new GetNotesInput();
        getNotesInput.setSize(9);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/getNotes").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(getNotesInput))).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andDo(MockMvcRestDocumentation.document("getNotes",
                        PayloadDocumentation.requestFields(getNotesInputFields.withPath("size").description("列表长度")),
                        PayloadDocumentation.responseFields().andWithPrefix("[].", note)
                        )
                );
    }

    @Test
    public void getNote() throws Exception {

        RequestBuilder requestBuilder = RestDocumentationRequestBuilders.get("/getNote").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(new GetNotesInput()));
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isOk();
        resultActions = resultActions.andExpect(resultMatcher);

        ResponseFieldsSnippet responseFieldsSnippet = PayloadDocumentation.responseFields(note);
        RestDocumentationResultHandler resultHandler = MockMvcRestDocumentation.document("getNote",  responseFieldsSnippet);

        resultActions.andDo(resultHandler);
    }

    @Test
    public void hello() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcRestDocumentation.document("index", PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("hello").type(JsonFieldType.STRING).description("哈哈哈"))));
    }


    private FieldDescriptor[] note = new FieldDescriptor[]{
            PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("日报"),
            PayloadDocumentation.fieldWithPath("cts").type(JsonFieldType.NUMBER).description("创建日期"),
            PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("内容"),
            PayloadDocumentation.fieldWithPath("author.name").type(JsonFieldType.STRING).description("作者姓名")};


    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return PayloadDocumentation.fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}