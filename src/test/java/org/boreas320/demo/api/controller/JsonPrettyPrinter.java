package org.boreas320.demo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by hurricane on 2017/12/31.
 */
@Component
public class JsonPrettyPrinter {


    @Autowired
    private ObjectMapper objectMapper;

    public String prettyPrint(String jsonString) {
        if (!StringUtils.isEmpty(jsonString))
            try {
                Object o = objectMapper.readValue(jsonString, Object.class);
                jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return jsonString;
    }
}
