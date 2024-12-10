package com.ruoyi.project.tool.parse.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义正则注解
 *
 * @author maohaitao
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD})
@Repeatable(Regs.class)
public @interface Reg {

    /**
     * 正则表达式
     */
    String value();

    /**
     * 正则Pattern index，默认为1
     */
    int index() default 1;

    /**
     * 支持换行模式：Pattern.DOTALL，默认不支持
     */
    boolean supportWrap() default false;
}
