package com.example.demo.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.Type;

public class MapperUtils {

    private MapperUtils() {
        throw new AssertionError();
    }

    public static <T> T convert(Object object, Class<T> clazz) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(object, clazz);
    }

    public static <T> T convert(Object object, Type clazz) {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(object, clazz);
    }
}