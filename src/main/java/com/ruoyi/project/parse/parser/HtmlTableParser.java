package com.ruoyi.project.parse.parser;


import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.DefaultCell;
import com.ruoyi.project.parse.domain.HtmlTable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes"})
@Slf4j
@AllArgsConstructor
public class HtmlTableParser extends AbstractTableParser<String> {

    /**
     * 解析pdf，返回pdf表格数据
     */
    @Override
    public List<HtmlTable> parse(String dataSource) {
        try {
            Document document = Jsoup.parse(new File(dataSource), "UTF-8");
            return table(document);
        } catch (IOException e) {
            throw new ServiceException("解析失败");
        }
    }

    /**
     * 解析表格
     */
    private List<HtmlTable> table(Document document) {
        Elements tables = document.select("table");
        List<HtmlTable> htmlTables = Lists.newArrayList();
        tables.forEach(element -> {
            HtmlTable htmlTable = new HtmlTable();
            Elements trs = element.select("tr");
            trs.forEach(tr -> {
                Elements th = tr.select("th");
                if (CollUtil.isNotEmpty(th)) {
                    htmlTable.getThList()
                            .addAll(th.stream().map(HtmlTableParser::getDefaultCell).collect(Collectors.toList()));
                }
                Elements td = tr.select("td");
                if (CollUtil.isNotEmpty(td)) {
                    // 剔除hidden单元格
                    htmlTable.getTdList().add(
                            td.stream().filter(e -> !e.hasAttr("hidden")).map(HtmlTableParser::getDefaultCell)
                                    .collect(Collectors.toList()));
                }
            });
            htmlTables.add(htmlTable);
        });
        htmlTables.forEach(htmlTable -> {
            List<? extends Cell> th = htmlTable.getTh();
            List<List<Cell>> tdList = htmlTable.getTdList();
            // 兼容某些页面格式不规范问题
            if (CollUtil.isEmpty(th) && CollUtil.isNotEmpty(tdList) && tdList.get(0).size() > 2) {
                htmlTable.setThList(tdList.get(0));
                htmlTable.setData(tdList.subList(1, tdList.size()));
            }
        });
        return htmlTables;
    }

    private static DefaultCell getDefaultCell(Element r) {
        DefaultCell cell = new DefaultCell();
        cell.setText(r.text());
        return cell;
    }
}
