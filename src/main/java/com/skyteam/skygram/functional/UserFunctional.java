package com.skyteam.skygram.functional;

import com.skyteam.skygram.dto.SearchResponseDTO;
import com.skyteam.skygram.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UserFunctional {

    public static final BiFunction<List<User>, String, List<SearchResponseDTO>> USERNAME_START_WITH = (users, s) -> users.stream()
            .filter(user -> user.getUsername().startsWith(s))
            .map(user -> new SearchResponseDTO(user.getId(), null, user.getUsername(), user.getFirstName() + " " + user.getLastName()))
            .collect(Collectors.toList());

    public static final BiFunction<List<User>, LocalDate, Long> NUM_OF_REGISTRATIONS = (users, date) -> users.stream()
            .map(User::getSignupDate)
            .filter(signupDate -> signupDate.toLocalDate().equals(date))
            .count();
}
