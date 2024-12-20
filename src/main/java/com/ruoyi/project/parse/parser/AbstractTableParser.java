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


}
