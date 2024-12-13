package com.ruoyi.project.parse.parser;


import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.DefaultCell;
import com.ruoyi.project.parse.domain.WordTable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class WordTableParser extends AbstractTableParser<String> {

    public static final String DOC = "doc";
    public static final String DOCX = "docx";

    /**
     * POI会将合并单元格特殊分割
     * ①纵向合并
     * 表格显示（merge代表合并单元格）：
     * ———————————————
     * | 1 |     | data|
     * |———|merge|—————|
     * | 2 |     | data|
     * |———|—————|—————|
     * | 3 | data| data|
     * ———————————————
     * 解析：
     * 1 merge data
     * 2 null  data
     * 3 data  data
     *
     * ======================
     *
     * ②横向合并
     * 表格显示（合并代表合并单元格）：
     * ———————————————
     * | 1 |   merge   |
     * |———|————— —————|
     * | 2 | data| data|
     * |———|—————|—————|
     * | 3 | data| data|
     * ———————————————
     * 解析：
     * 1 合并
     * 2 data data
     * 3 data data
     * 【第一行为两列，其他行为三列】
     *
     * 总结：POI处理横向合并单元格不会冗余其他行的列数（和pdf解析不同），处理纵向合并单元格和PDF解析一致。
     * 不会发生额外空列情况，不需要处理表格跨页的情况。
     */
    @Override
    public List<WordTable> parse(String dataSource) {
        if (dataSource == null) {
            return ListUtil.empty();
        }
        List<WordTable> wordTableList = Lists.newArrayList();
        try (InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(dataSource)))) {
            FileMagic fileMagic = FileMagic.valueOf(is);
            if (fileMagic == FileMagic.OOXML) {
                wordTableList = parseDocx(is);
            } else if (fileMagic == FileMagic.OLE2) {
                wordTableList = parseDoc(is);
            } else {
                throw new RuntimeException("不支持的word文件格式！" + dataSource);
            }
        } catch (Exception e) {
            log.error("解析出现异常!path:{}", dataSource, e);
        }

        //删除空表
        delEmptyTable(wordTableList);
        checkPdfTh(wordTableList);
        //删除各表空行
        delEmptyTh(wordTableList);
        AbstractTableParser.handleExt(wordTableList);
        return wordTableList;
    }

    private List<WordTable> parseDoc(InputStream is) throws IOException {
        List<WordTable> wordTableList = Lists.newArrayList();
        try (HWPFDocument document = new HWPFDocument(is)) {
            Range range = document.getRange();
            TableIterator tableIterator = new TableIterator(range);
            while (tableIterator.hasNext()) {
                Table table = tableIterator.next();
                WordTable wordTable = new WordTable();
                List<List<Cell>> rowList = Lists.newArrayList();
                for (int i = 0; i < table.numRows(); i++) {
                    TableRow row = table.getRow(i);
                    List<Cell> cellList = Lists.newArrayList();
                    for (int j = 0; j < row.numCells(); j++) {
                        cellList.add(new DefaultCell(row.getCell(j).text().replaceAll("\\s*|\r|\n|\t|\\a", "").trim()));
                    }
                    rowList.add(cellList);
                }
                wordTable.setNotEmptyData(rowList);
                wordTableList.add(wordTable);
            }
        }
        return wordTableList;
    }

    private List<WordTable> parseDocx(InputStream is) throws IOException {
        List<WordTable> wordTableList = Lists.newArrayList();
        try (XWPFDocument document = new XWPFDocument(is)) {
            List<XWPFTable> tableList = document.getTables();
            for (XWPFTable table : tableList) {
                WordTable wordTable = new WordTable();
                List<List<Cell>> rowList = table.getRows().stream().map(row -> row.getTableCells().stream().map(
                                        cell -> new DefaultCell(cell.getText().replaceAll("\\s*|\r|\n|\t|\\a", "").trim()))
                                .map(Cell.class::cast)
                                .collect(Collectors.toList()))
                        .collect(Collectors.toList());
                wordTable.setNotEmptyData(rowList);
                wordTableList.add(wordTable);
            }
        }
        return wordTableList;
    }
}
