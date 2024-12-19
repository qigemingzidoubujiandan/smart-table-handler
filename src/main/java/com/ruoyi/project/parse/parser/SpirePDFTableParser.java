package com.ruoyi.project.parse.parser;

import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.DefaultCell;
import com.ruoyi.project.parse.domain.PDFTable;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class SpirePDFTableParser extends AbstractTableParser<String> {

    private static final int LIMIT_PAGE_SIZE = 10;

    /**
     * <p>
     * free版本只能解析前10页
     * </p>
     */
    @Override
    public List<PDFTable> parse(String dataSource) {
        PdfDocument pdf = null;
        List<PDFTable> pdfTableList = new ArrayList<>();
        try {
            if (dataSource != null) {
                pdf = new PdfDocument();
                pdf.loadFromFile(dataSource);

                int pageCount = pdf.getPages().getCount();
                if (pageCount > LIMIT_PAGE_SIZE) {
                    parseTableCrack(pdfTableList, pdf);
                } else {
                    parseTable(pdfTableList, pdf);
                }

                //删除空表
                delEmptyTable(pdfTableList);
                AbstractTableParser.handleExt(pdfTableList);
            }
        } catch (Exception e) {
            if (pdf != null) {
                pdf.close();
            }
            log.warn("[SpirePDFTableParser]解析出现异常!path:{}", dataSource, e);
            log.warn("[SpirePDFTableParser] 降级----- TabulaPDFTableParser start !path:{}", dataSource, e);
            TabulaPDFTableParser parser = new TabulaPDFTableParser();
            log.warn("[SpirePDFTableParser] 降级----- TabulaPDFTableParser end !path:{}", dataSource, e);
            return parser.parse(dataSource);
        } finally {
            if (pdf != null) {
                pdf.close();
            }
        }
        return pdfTableList;
    }

    /**
     * 破解10页限制
     */
    private void parseTableCrack(List<PDFTable> pdfTableList, PdfDocument pdf) {
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
        for (int page = 0; page < pdf.getPages().getCount(); page++) {
            PdfTable[] tableList = extractor.extractTable(page);
            if (tableList != null && tableList.length > 0) {
                for (PdfTable table : tableList) {
                    PDFTable pdfTable = convertToPDFTable(table);
                    pdfTableList.add(pdfTable);
                }
            }
            // 逻辑删除doc对象，破解Spire.PDF限制10页问题
            pdf.getPages().removeAt(page);
            page--;
        }
    }

    /**
     * 存在10页限制
     */
    private void parseTable(List<PDFTable> pdfTableList, PdfDocument pdf) {
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);
        for (int page = 0; page < pdf.getPages().getCount(); page++) {
            PdfTable[] tableList = extractor.extractTable(page);
            if (tableList != null && tableList.length > 0) {
                for (PdfTable table : tableList) {
                    if (Objects.isNull(table)) {
                        continue;
                    }
                    PDFTable pdfTable = convertToPDFTable(table);
                    pdfTableList.add(pdfTable);
                }
            }
        }
    }

    private PDFTable convertToPDFTable(PdfTable table) {
        PDFTable pdfTable = new PDFTable();
        List<List<Cell>> pdfRow = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            List<Cell> pdfCell = new ArrayList<>();
            for (int j = 0; j < table.getColumnCount(); j++) {
                Cell pdfTableCell = new DefaultCell();
                pdfTableCell.setText(table.getText(i, j).replaceAll("\\s*|\r|\n|\t", ""));
                pdfCell.add(pdfTableCell);
            }
            pdfRow.add(pdfCell);
        }
        pdfTable.setData(pdfRow);
        return pdfTable;
    }
}
