package com.ruoyi.project.system.parse.parse.extractor;


import com.ruoyi.project.system.parse.parse.domain.Table;

import java.util.List;

/**
 * 表格抽取
 * @author chenl
 */
public  interface TableParseRule<T> {

    /**
     * 执行表格抽取解析
     */
    T execute(List<? extends Table> tableList);

}
