package com.skyteam.skygram.common;

import java.io.Serializable;

public class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer size;

    public PageDTO(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}