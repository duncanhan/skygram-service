package com.skyteam.skygram.functional;

@FunctionalInterface
public interface FiveFunctional<O, T, U, V, W, R> {
    R apply(O o,T t, U u, V v, W w);
}
