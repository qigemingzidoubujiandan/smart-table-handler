package com.ruoyi.project.parse.parser;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.project.parse.domain.Cell;
import com.ruoyi.project.parse.domain.DefaultCell;
import com.ruoyi.project.parse.domain.PDFTable;
import com.ruoyi.project.parse.util.YamlUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * python PDFPlumber 可以解析无规则表格，ex：表格边框未封闭
 */
@Slf4j
@AllArgsConstructor
public class PyPDFPlumberTableParser extends AbstractTableParser<String> {

    /**
     * PDFPlumber库解析表格参数配置
     */
    private String tableSetting;
    /**
     * 字符集
     */
    private String standardCharset;

    public static final String DEFAULT_TABLE_SETTING =
            "{'vertical_strategy':'text','horizontal_strategy':'lines','intersection_tolerance':10}";
    public static final String DEFAULT_TABLE_BHSETTING =
            "{'vertical_strategy':'text','horizontal_strategy':'lines','intersection_tolerance':15}";

    @Override
    public List<PDFTable> parse(String dataSource) {
        if (dataSource == null) {
            return ListUtil.empty();
        }
        Process process = null;
        try {
            String command = genShellCommand(dataSource);
            process = Runtime.getRuntime().exec(command);
            // 避免waitFor死锁
            String pyPrint = readStream(process.getInputStream());
            String errorMsg = readStream(process.getErrorStream());
            int waitFor = process.waitFor();
            if (waitFor != 0) {
                throw new RuntimeException("执行py脚本异常!" + errorMsg);
            }
            List<PDFTable> tableList = convertToPdfTable(pyPrint);

            delEmptyTable(tableList);
            AbstractTableParser.handleExt(tableList);
            return tableList;
        } catch (Exception e) {
            log.error("解析出现异常!path:{}", dataSource, e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return ListUtil.empty();
    }

    private List<PDFTable> convertToPdfTable(String pyPrint) {
        if (StrUtil.isBlank(pyPrint)) {
            return ListUtil.empty();
        }
        List<PDFTable> pdfTableList = new ArrayList<>();
        List<List> tableList = JSONUtil.toList(pyPrint, List.class);

        tableList.forEach(
                table -> {
                    PDFTable pdfTable = new PDFTable();
                    List<List<Cell>> pdfRowList = new ArrayList<>();
                    for (Object rowObj : table) {
                        List<Cell> pdfRow = new ArrayList<>();
                        List<String> row = JSONUtil.toList(JSONUtil.parseArray(rowObj), String.class);
                        for (String cellText : row) {
                            DefaultCell pdfTableCell = Optional.ofNullable(cellText)
                                    .map(c -> new DefaultCell(cellText.replaceAll("\\s*|\n|\t|\r", "")))
                                    .orElse(new DefaultCell(""));
                            pdfRow.add(pdfTableCell);
                        }
                        pdfRowList.add(pdfRow);
                    }
                    pdfTable.setData(pdfRowList);
                    pdfTableList.add(pdfTable);
                }
        );
        return pdfTableList;
    }

    private String readStream(InputStream is) throws IOException {
        return IOUtils.toString(is, standardCharset);
    }

    private String genShellCommand(String path) throws IOException {
        StringBuilder shellBuilder = new StringBuilder();
        shellBuilder.append("python");
        shellBuilder.append(" ");
        shellBuilder.append(getPyScriptPath());
        shellBuilder.append(" ");
        shellBuilder.append(path);
        shellBuilder.append(" ");
        shellBuilder.append(tableSetting);
        return shellBuilder.toString();
    }

    private String getPyScriptPath() throws IOException {
        String path;
        boolean local = YamlUtil.isLocal();
        if (local) {
            ClassPathResource classPathResource = new ClassPathResource("python/pdf_parse.py");
            path = classPathResource.getFile().getPath();
        } else {
            path = YamlUtil.get("python.parser-path");
        }
        return path;
    }
}
