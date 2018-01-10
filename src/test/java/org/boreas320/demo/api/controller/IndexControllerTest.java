package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.boreas320.demo.api.model.GetNoteInput;
import org.boreas320.demo.api.model.GetNotesInput;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by xiangshuai on 2017/12/29.
 */

/**
 * 将spring环境于junit进行整合,获取ApplicationContext,注入各类bean
 */
@RunWith(SpringRunner.class)
/**
 * 整合SpringBoot,上面的SpringRunner只是说明将spring环境整合到junit当中,而SpringBootTest说明是通过SpringBoot来获取真正的spring环境的
 * 包括但不限于bean的获取与注入,webApplicationContext环境的获取与注入,web服务端口随机化等
 */
@SpringBootTest
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    /**
     * 用来表明文档片段的生成位置
     */
    @Rule//类似于Before注解
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    /**
     * 此方法虽然会在每个test method前都调用到,但是webApplicationContext仅会生成一次,因此不会导致测试时间过长
     */
    @Before
    public void setUp() {

        MockMvcRestDocumentationConfigurer mockMvcRestDocumentationConfigurer = MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation);

        //配置需要生成的文档片段默认为http request,http response, curl, httpie
        Snippet[] snippets = {HttpDocumentation.httpRequest(), HttpDocumentation.httpResponse(),/*CliDocumentation.curlRequest()*/};
        mockMvcRestDocumentationConfigurer.snippets().withDefaults(snippets);

        //格式化文档片段,json字符串的格式化
        mockMvcRestDocumentationConfigurer.operationPreprocessors()
                .withRequestDefaults(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HttpHeaders.CONTENT_LENGTH))
                .withResponseDefaults(Preprocessors.prettyPrint(), Preprocessors.removeHeaders(HttpHeaders.CONTENT_LENGTH));

        //配置需要在文档中显示的url前缀信息
        mockMvcRestDocumentationConfigurer.uris().withHost("api.xm.test.sankuai.com").withScheme("https").withPort(443);

        //将webApplicationContext注册到mockMvc中,用以进行mock测试
        DefaultMockMvcBuilder mockMvcBuilder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);

        //配置请求的contextPath,此处仅为了配置contextPath而创建了一个默认mockHttpServletRequestBuilder,详细说明在下面getNote方法中介绍
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(URI.create("http://neixin.cn"));
        mockHttpServletRequestBuilder.contextPath("/mtinfo");
        mockMvcBuilder.defaultRequest(mockHttpServletRequestBuilder);

        //将mockMvcRestDocumentationConfigurer应用到mockMvc中
        mockMvcBuilder = mockMvcBuilder.apply(mockMvcRestDocumentationConfigurer);
        this.mockMvc = mockMvcBuilder.build();
    }

    private FieldDescriptor[] note = new FieldDescriptor[]{
            PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("日报"),
            PayloadDocumentation.fieldWithPath("cts").type(JsonFieldType.NUMBER).description("创建日期"),
            PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("内容"),
            PayloadDocumentation.fieldWithPath("author.name").type(JsonFieldType.STRING).description("作者姓名")};

    @Test
    public void getNote() throws Exception {
        GetNoteInput getNoteInput = new GetNoteInput();
        getNoteInput.setId(123l);

        //请求体片段
        RequestFieldsSnippet requestFieldsSnippet =
                PayloadDocumentation
                        .requestFields(
                                PayloadDocumentation
                                        .fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("备忘录id")
                                        .attributes(Attributes.key("constraints").value("不能小于0")));
        //响应体片段
        ResponseFieldsSnippet responseFieldsSnippet =
                PayloadDocumentation
                        .responseFields(
                                PayloadDocumentation
                                        .fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("日报"),
                                PayloadDocumentation
                                        .fieldWithPath("cts")
                                        .type(JsonFieldType.NUMBER)
                                        .description("创建日期"),
                                PayloadDocumentation
                                        .fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("内容"),
                                PayloadDocumentation
                                        .fieldWithPath("author.name")
                                        .type(JsonFieldType.STRING)
                                        .description("作者姓名"));
        //Header定义
        RequestHeadersSnippet requestHeadersSnippet =
                HeaderDocumentation
                        .requestHeaders(
                                HeaderDocumentation
                                        .headerWithName("u")
                                        .description("用户uid"),
                                HeaderDocumentation
                                        .headerWithName("uu")
                                        .description("验证信息"),
                                HeaderDocumentation
                                        .headerWithName("al")
                                        .description("altoken"));

        //流式编程 mockMvc + requestBuilder -> resultActions + resultMatcher -> resultActions + resultHandler
        //requestBuilder产生请求
        RequestBuilder requestBuilder =
                RestDocumentationRequestBuilders
                        .post("/mtinfo/getNote")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(getNoteInput))
                        .header("u", 6856967)
                        .header("uu", "xxxx")
                        .header("al", "ttttt");
        //生成结果操作句柄
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        //resultMatcher用来判断请求结果是否满足测试要求,可配置多个
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isOk();
        resultActions = resultActions.andExpect(resultMatcher);

        //resultHandler对请求及响应进行处理,主要用来打印日志生成文档
        RestDocumentationResultHandler resultHandler = MockMvcRestDocumentation.document("getNote", requestHeadersSnippet, requestFieldsSnippet, responseFieldsSnippet);
        resultActions.andDo(resultHandler);
    }

    @Test
    public void getNotes() throws Exception {
        ConstrainedFields getNotesInputFields = new ConstrainedFields(GetNotesInput.class);
        GetNotesInput getNotesInput = new GetNotesInput();
        getNotesInput.setSize(9);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/mtinfo/getNotes").contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(getNotesInput))).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andDo(MockMvcRestDocumentation.document("getNotes",
                        PayloadDocumentation.requestFields(getNotesInputFields.withPath("size").description("列表长度")),
                        PayloadDocumentation.responseFields().andWithPrefix("[].", note)
                        )
                );
    }


    @Test
    public void hello() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/mtinfo")).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcRestDocumentation.document("index", PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("hello").type(JsonFieldType.STRING).description("哈哈哈"))));
    }


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