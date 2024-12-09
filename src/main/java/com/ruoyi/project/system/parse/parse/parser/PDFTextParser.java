package com.ruoyi.project.system.parse.parse.parser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.project.system.parse.parse.parser.model.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PDFTextParser extends AbstractTextParser<String> {

    private Integer startPage = 1;
    private Area area;
    private Integer patternFlags;
    private Set<String> filterReg;

    public PDFTextParser(Integer startPage) {
        this.startPage = startPage < 1 ? 1 : startPage;
    }

    public PDFTextParser(Area area) {
        this.area = area;
    }

    public PDFTextParser(Area area, Integer patternFlags, Set<String> filterReg) {
        this.area = area;
        this.patternFlags = patternFlags;
        this.filterReg = filterReg;
    }

    public PDFTextParser(Integer patternFlags, Set<String> filterReg) {
        this.patternFlags = patternFlags;
        this.filterReg = filterReg;
    }

    @SneakyThrows
    @Override
    public String parse(String dataSource) {
        String text = readFile(dataSource);
        // 过滤页眉页脚等
        return filterText(text);
    }

    /**
     * 一次获取整个文件的内容
     */
    public String readFile(String fileName) throws Exception {
        PDDocument doc = null;
        RandomAccessFile is = null;
        try {
            File file = new File(fileName);
            is = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(is);
            parser.parse();
            doc = parser.getPDDocument();
            return Objects.isNull(area) ? doRead(doc) : doReadByArea(doc);
        } catch (Exception e) {
            log.error("error : {}", e.getMessage(), e);
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return "";
    }

    private String doRead(PDDocument document) throws IOException {
        if (startPage > document.getNumberOfPages()) {
            return "";
        }
        PDFTextStripper stripper = new PDFTextStripper();
        if (startPage > 1) {
            stripper.setStartPage(startPage);
            stripper.setEndPage(document.getNumberOfPages());
        }
        stripper.setSortByPosition(true);
        return stripper.getText(document);
    }

    private String doReadByArea(PDDocument document) throws IOException {
        if (startPage > document.getNumberOfPages()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = startPage - 1; i < document.getNumberOfPages(); i++) {
            PDPage page = document.getPage(i);
            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
            textStripper.addRegion("Region", area.convertToRectangle(page));
            textStripper.extractRegions(page);
            // textStripper.setSortByPosition(true);
            sb.append(textStripper.getTextForRegion("Region"));
        }
        return sb.toString();
    }

    private String filterText(String text) {
        if (CollUtil.isEmpty(this.filterReg) || StrUtil.isBlank(text)) {
            return text;
        }
        for (String r : this.filterReg) {
            text = text.replaceAll(r, "\n");
        }
        return text;
    }
}
