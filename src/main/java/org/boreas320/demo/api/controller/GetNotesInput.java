package org.boreas320.demo.api.controller;

import javax.validation.constraints.Size;

/**
 * Created by xiangshuai on 2017/12/29.
 */
public class GetNotesInput {
    @Size(min = 1)
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
