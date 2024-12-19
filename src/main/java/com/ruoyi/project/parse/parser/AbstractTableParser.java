package com.ruoyi.project.parse.parser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.Table;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.ruoyi.project.parse.extractor.unit.UnitExtractor.amountUnitExtract;

public abstract class AbstractTableParser<T> implements IParser<T, List<? extends Table>> {

    /**
     * 处理扩展信息，先放这里吧
     */
    public static void handleExt(List<? extends Table> tables) {
        handleExt(tables, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void handleExt(List<? extends Table> tables, List<Function<String, String>> func) {
        if (tables == null || tables.isEmpty()) {
            return;
        }
        tables.forEach(r -> {
            List<? extends Cell> list = r.getTh();
            if (CollUtil.isNotEmpty(list)) {
                list.forEach(th -> {
                    String ext =
                            CollUtil.isEmpty(func) ? amountUnitExtract(th.text()) : amountUnitExtract(func, th.text());
                    if (CharSequenceUtil.isNotEmpty(ext)) {
                        th.setExt(ext);
                    }
                });
            }
        });
    }

    protected void delEmptyTable(List<? extends Table> tables) {
        Iterator<? extends Table> it = tables.iterator();
        while (it.hasNext()) {
            Table table = it.next();
            if (table.getData().size() == 0) {
                it.remove();
            }
        }
    }
}
