package com.ruoyi.project.parse.parser;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.project.parse.domain.Table;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 复合解析器
 * @author chenl
 */
public class CompositeParser extends AbstractTableParser<String> {

    private final List<AbstractTableParser<String>> parsers = new ArrayList<>();

    public CompositeParser(List<Class<? extends AbstractTableParser<String>>> parserClasses) {
        try {
            for (Class<? extends AbstractTableParser> parserClass : parserClasses) {
                parsers.add(parserClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new ServiceException("不支持组合");
        }
    }

    @Override
    public List<? extends Table> parse(String s) {
        return parsers.stream()
                .map(parser -> parser.parse(s)).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
