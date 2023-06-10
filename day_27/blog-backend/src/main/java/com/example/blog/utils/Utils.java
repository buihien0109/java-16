package com.example.blog.utils;

import java.util.List;
import java.util.stream.IntStream;

public class Utils {
    public static List<Integer> createList(Integer size) {
        List<Integer> range = IntStream.rangeClosed(1, size)
                .boxed().toList();

        return range;
    }
}
