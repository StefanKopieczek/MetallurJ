package com.stefankopieczek.metallurj;

import java.util.function.Function;

public interface CostFunction<T> extends Function<T, Integer> {
    Integer apply(T t);
}