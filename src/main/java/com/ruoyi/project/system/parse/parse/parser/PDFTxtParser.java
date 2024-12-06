package com.ruoyi.project.system.parse.parse.parser;

import com.ruoyi.project.system.parse.parse.parser.model.Area;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;

import java.io.File;

/**
 * @author jianwei
 * @date 2022/9/28
 */
@Slf4j
@Builder
@AllArgsConstructor
public class PDFTxtParser extends AbstractTextParser<String> {

    private Integer pageNumber;
    private boolean isAllPage;
    private Area area;

    public PDFTxtParser() {
        this(null, true, null);
    }

    @SneakyThrows
    @Override
    public String parse(String dataSource) {
        return doParse(dataSource);
    }

    private String doParse(String dataSource) throws Exception {
        StringBuilder builder = new StringBuilder();
        PDDocument document;
        ObjectExtractor oe = null;
        try {
            document = PDDocument.load(new File(dataSource));

            oe = new ObjectExtractor(document);
            if (isAllPage) {
                PageIterator iterator = oe.extract();
                while (iterator.hasNext()) {
                    Page page = iterator.next();
                    if (area == null) {
                        page.getText().forEach(t -> builder.append(t.getText()));
                    } else {
                        page.getText(area.convertToRectangle(page)).forEach(t -> builder.append(t.getText()));
                    }
                }
            } else {
                // 默认只解析1页
                Page extract = oe.extract(pageNumber);
                if (area == null) {
                    extract.getText().forEach(t -> builder.append(t.getText()));
                } else {
                    extract.getText(area.convertToRectangle(extract)).forEach(t -> builder.append(t.getText()));
                }
            }
            return builder.toString();
        } finally {
            if (null != oe) {
                oe.close();
            }
        }
    }
}
