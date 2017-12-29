package org.boreas320.demo.api.model;

/**
 * Created by xiangshuai on 2017/12/29.
 */
public class Person {
    public Person(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
