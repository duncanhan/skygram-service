package com.skyteam.skygram.functional;

import com.skyteam.skygram.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CommonFunctional {

    public static final BiFunction<List<User>, Pageable, List<User>> pagingUser = (users, page) -> users.stream()
            .skip(page.getPageNumber() * page.getPageSize())
            .limit(page.getPageSize())
            .collect(Collectors.toList());
}
