package com.skyteam.skygram.common;

import java.io.Serializable;

public class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int size;

    public PageDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}