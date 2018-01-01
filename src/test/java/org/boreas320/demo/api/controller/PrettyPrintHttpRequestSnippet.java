package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.http.HttpRequestSnippet;
import org.springframework.restdocs.operation.Operation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hurricane on 2017/12/31.
 */
@Component
public class PrettyPrintHttpRequestSnippet extends HttpRequestSnippet {

    @Autowired
    private JsonPrettyPrinter prettyPrinter;

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        Map<String, Object> model = super.createModel(operation);
        String requestBody = (String) model.get("requestBody");
        requestBody = prettyPrinter.prettyPrint(requestBody);
        model.put("requestBody", requestBody);
        return model;
    }
}
