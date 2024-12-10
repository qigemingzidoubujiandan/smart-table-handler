package com.ruoyi.project.tool.parse.parser;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.project.tool.parse.domain.PDFTable;
import com.ruoyi.project.tool.parse.parser.model.Area;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import technology.tabula.CommandLineApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class TabulaPDFTableParser extends AbstractTableParser<String> {

    private Area area;

    /**
     * 解析pdf，返回pdf表格数据
     */
    @Override
    public List<PDFTable> parse(String dataSource) {
        return dataSource != null ? parsePdf(dataSource) : ListUtil.empty();
    }

    protected List<PDFTable> parsePdf(String request) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] args = new String[]{"-f=JSON", "-p=all", request, "-l"};
        try {
            //-f导出格式,默认CSV  (一定要大写)
            //-p 指导出哪页,all是所有
            //path　D:\\1xx.pdf
            //-l 强制使用点阵模式提取PDF　（关键在于这儿）
            // -a/--area = Portion of the page to analyze. Example: --area 269.875,12.75,790.5,561. Accepts top,left,bottom,right i.e. y1,x1,y2,x2
            if (Objects.nonNull(this.area)) {
                Optional<String> areaOpt = this.area.convertToRectangleStr(request);
                if (areaOpt.isPresent()) {
                    args = new String[]{"-f=JSON", "-p=all", request, "-l", "-a=" + areaOpt.get()};
                }

            }
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(CommandLineApp.buildOptions(), args);
            CommandLineApp commandLineApp = new CommandLineApp(stringBuilder, cmd);
            commandLineApp.extractTables(cmd);
        } catch (Exception e) {
            log.error("【TabulaParse】 error ：{}", e.getMessage());
        }
        //检验各表格表头是否为空 如果为空 则删除表头 重新校验
        List<PDFTable> pdfTables = new ArrayList<PDFTable>();
        try {
            pdfTables = JSONUtil.toList(stringBuilder.toString(), PDFTable.class);
        } catch (Exception e) {
            log.error("pdfTables Null error : {}", e.getMessage());
        }
        //删除空表
        delEmptyTable(pdfTables);
        checkPdfTh(pdfTables);
        //删除各表空行
        delEmptyTh(pdfTables);
        AbstractTableParser.handleExt(pdfTables);

        return pdfTables;
    }


    private void exe(String path) {
        List<PDFTable> pdfTables = parsePdf(path);
    }
}
