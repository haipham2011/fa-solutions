package com.example.fasolutionsbackend.utils;

import java.util.stream.Stream;

public class Common {
    public static <T> Stream<T> getSliceOfStream(Stream<T> stream, int startIndex,
                                                  int endIndex)
    {
        return stream
                // specify the number of elements to skip
                .skip(startIndex)

                // specify the no. of elements the stream
                // that should be limited
                .limit(endIndex - startIndex + 1);
    }
}
