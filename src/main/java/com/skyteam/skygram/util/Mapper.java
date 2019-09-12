package com.skyteam.skygram.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    private static final ModelMapper INSTANCE = new ModelMapper();

    private Mapper() {
        INSTANCE.getConfiguration().setAmbiguityIgnored(true);
    }

    public static <S, T> T map(S source, Class<T> targetClass) {
        return INSTANCE.map(source, targetClass);
    }

    public static <S, T> T map(S source, Class<T> targetClass, PropertyMap<S, T> propertyMap) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.addMappings(propertyMap);
        return mapper.map(source, targetClass);
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        List<T> list = new ArrayList<>();
        for (S aSource : source) {
            list.add(map(aSource, targetClass));
        }

        return list;
    }

    public static <S, T> Page<T> mapPage(Page<S> source, Class<T> targetClass) {
        return new PageImpl<>(
                mapList(source.getContent(), targetClass),
                PageRequest.of(source.getNumber(), source.getSize(), source.getSort()),
                source.getTotalElements()
        );
    }

    public static <S, T> Page<T> mapPage(Page<S> source, Class<T> targetClass, PropertyMap<S, T> propertyMap) {
        List<S> sourceList = source.getContent();
        List<T> list = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        for (S aSourceList : sourceList) {
            T target = modelMapper.map(aSourceList, targetClass);
            list.add(target);
        }

        return new PageImpl<>(
                list,
                PageRequest.of(source.getNumber(), source.getSize(), source.getSort()),
                source.getTotalElements()
        );
    }
}
