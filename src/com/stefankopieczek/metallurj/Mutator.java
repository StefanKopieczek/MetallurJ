package com.stefankopieczek.metallurj;

import java.util.List;

public interface Mutator<T> {
    T getRandomNeighbour(T t);
}
