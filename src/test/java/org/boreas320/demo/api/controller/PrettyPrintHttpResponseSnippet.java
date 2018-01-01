package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.http.HttpResponseSnippet;
import org.springframework.restdocs.operation.Operation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hurricane on 2017/12/31.
 */
@Component
public class PrettyPrintHttpResponseSnippet extends HttpResponseSnippet {


    @Autowired
    private JsonPrettyPrinter prettyPrinter;

    @Override
    protected Map<String, Object> createModel(Operation operation) {

        Map<String, Object> model = super.createModel(operation);
        String responseBody = (String) model.get("responseBody");
        responseBody = prettyPrinter.prettyPrint(responseBody);
        model.put("responseBody", responseBody);
        return model;

    }
}
