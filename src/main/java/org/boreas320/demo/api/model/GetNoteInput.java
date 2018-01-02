package org.boreas320.demo.api.model;

import javax.validation.constraints.Size;

/**
 * Created by hurricane on 2017/12/29.
 */
public class GetNoteInput {
    @Size(min = 1)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
