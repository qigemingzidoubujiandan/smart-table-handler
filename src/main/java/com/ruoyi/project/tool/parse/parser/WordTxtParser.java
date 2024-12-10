package com.ruoyi.project.tool.parse.parser;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@AllArgsConstructor
public class WordTxtParser extends AbstractTextParser<String> {

    @SneakyThrows
    @Override
    public String parse(String dataSource) {
        return doParse(dataSource);
    }

    private String doParse(String dataSource) throws Exception {
        String text;
        try (InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get(dataSource)))) {
            FileMagic fileMagic = FileMagic.valueOf(is);
            if (fileMagic == FileMagic.OOXML) {
                text = getDocxText(is);
            } else if (fileMagic == FileMagic.OLE2) {
                text = getDocText(is);
            } else {
                throw new RuntimeException("不支持的word文件格式！" + dataSource);
            }
        }
        return text;
    }

    private String getDocText(InputStream is) throws IOException {
        try (HWPFDocument document = new HWPFDocument(is)) {
            return document.getDocumentText();
        }
    }

    private String getDocxText(InputStream is) throws IOException {
        try (XWPFDocument document = new XWPFDocument(is); XWPFWordExtractor extractor = new XWPFWordExtractor(
                document)) {
            return extractor.getText();
        }
    }
}
