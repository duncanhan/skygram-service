package com.skyteam.skygram.util;

import com.skyteam.skygram.common.Constants;
import com.skyteam.skygram.common.PageDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtil {

    public static Pageable initPage(PageDTO pageDTO) {
        int page = pageDTO.getPage();
        int size = pageDTO.getSize();

        if (page < 1) {
            page = Constants.DEFAULT_PAGE;
        }
        if (size < 1) {
            size = Constants.DEFAULT_SIZE;
        }
        return PageRequest.of(page, size);
    }

    public static Pageable initPage(PageDTO pageDTO, Sort sort) {
        int page = pageDTO.getPage();
        int size = pageDTO.getSize();

        if (page < 1) {
            page = Constants.DEFAULT_PAGE;
        }
        if (size < 1) {
            size = Constants.DEFAULT_SIZE;
        }
        return PageRequest.of(page, size, sort);
    }
}
