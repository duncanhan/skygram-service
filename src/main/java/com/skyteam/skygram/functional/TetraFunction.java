package com.skyteam.skygram.functional;

@FunctionalInterface
public interface TetraFunction<S, T, U, V, R> {
    R apply(S s, T t, U u, V v);
}
