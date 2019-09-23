package com.skyteam.skygram.functional;

@FunctionalInterface
public interface QuadriFunction<T, U, V, W, R> {
    R apply(T t, U u, V v, W w);
}
