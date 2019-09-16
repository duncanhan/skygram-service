package com.skyteam.skygram.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer size;
}