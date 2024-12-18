package com.ruoyi.project.parse.extractor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parse.anno.Reg;
import com.ruoyi.project.parse.util.RegUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.ruoyi.project.parse.extractor.unit.UnitExtractConverter.handleAmountUnit;


@Slf4j
@Data
public abstract class AbstractAnnoTextExtractor<T> implements IExtractor<String, T> {

    private T t;

    protected AbstractAnnoTextExtractor() {
        t = ReflectUtil.newInstance(getGenericClazz());
    }

    @Override
    public T extract(String text) {
        matchingExtract(text);
        return t;
    }

    @SneakyThrows
    protected void matchingExtract(String text) {
        if (StrUtil.isBlank(text)) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Reg[] regList = field.getAnnotationsByType(Reg.class);
            if (ArrayUtil.isEmpty(regList)) {
                continue;
            }
            String parseRst = doReg(regList, text);
            if (StringUtils.isNotBlank(parseRst) && Objects.isNull(field.get(t))) {
                field.set(t, handleAmountUnit(parseRst));
            }
        }
    }

    private String doReg(Reg[] regList, String text) {
        for (Reg reg : regList) {
            Pattern pattern =
                    reg.supportWrap() ? Pattern.compile(reg.value(), Pattern.DOTALL) : Pattern.compile(reg.value());
            String matchText = RegUtil.applyReg(text, pattern, reg.index());
            if (StrUtil.isNotBlank(matchText)) {
                return matchText;
            }
        }
        return null;
    }

    /** 获取泛型Class **/
    @SuppressWarnings("unchecked")
    private Class<T> getGenericClazz() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = genericSuperclass.getActualTypeArguments();
        return (Class<T>) types[0];
    }
}
