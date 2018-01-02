package org.boreas320.demo.api.controller;

import org.boreas320.demo.api.model.GetNotesInput;
import org.junit.Test;
import org.springframework.restdocs.constraints.ConstraintDescriptions;

import java.util.List;

/**
 * Created by xiangshuai on 2017/12/29.
 */
public class OtherTest {
    @Test
    public void zhTest() {
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(GetNotesInput.class);
        List<String> size = constraintDescriptions.descriptionsForProperty("size");

        System.out.println(size);
    }
}
