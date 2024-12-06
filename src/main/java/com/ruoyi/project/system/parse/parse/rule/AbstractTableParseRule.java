package com.ruoyi.project.system.parse.parse.rule;


import lombok.Data;

@Data
public abstract class AbstractTableParseRule<E> implements TableParseRule<E> {

    private E parsedResult;

    private String[] conditions;

    /**
     * 期望解析的行数
     * !!! pdf 因为分页割裂的表格,这个参数尤为重要,会解析到期望解析的行数
     */
    private int expectParseRowSize;

    @Override
    public int expectParseRowSize() {
        return expectParseRowSize;
    }

    @Override
    public String[] condition() {
        return conditions;
    }

    @Override
    public E matchedData() {
        return parsedResult;
    }
}
