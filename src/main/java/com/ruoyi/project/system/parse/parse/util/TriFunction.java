package com.ruoyi.project.system.parse.parse.util;

/**
 * @author jianwei
 * @date 2023/5/18
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T t, U u, V v);
}
