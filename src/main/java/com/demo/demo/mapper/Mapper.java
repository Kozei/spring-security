package com.demo.demo.mapper;

public interface Mapper <S, T> {

    T map(S s);
    S mapReverse(T t);
}
