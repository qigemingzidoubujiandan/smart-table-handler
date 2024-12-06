package com.ruoyi.project.system.parse.parse.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.nodes.Document;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解析文本工具类
 */
@Slf4j
public class ParseTextUtil {

    public static String parseHtml(Document document, boolean removeSpace) {
        String text = document.text();
        return filterText(text, removeSpace);
    }

    @SneakyThrows
    public static String parsePdf(File file, boolean removeSpace) {
        PDDocument doc = null;
        RandomAccessFile is = null;
        try {
            is = new RandomAccessFile(file, "r");
            PDFParser parser = new PDFParser(is);
            parser.parse();
            doc = parser.getPDDocument();

            PDFTextStripper stripper = new PDFTextStripper();
            // stripper.setStartPage(1);
            // stripper.setEndPage(doc.getNumberOfPages());
            stripper.setSortByPosition(true);
            String text = stripper.getText(doc);
            return filterText(text, removeSpace);
        } catch (Exception e) {
            throw new RuntimeException("parse pdf text error！", e);
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public static String parseTabulaPdf(File file, boolean removeSpace) {
        StringBuilder builder = new StringBuilder();
        try (PDDocument document = PDDocument.load(file); ObjectExtractor oe = new ObjectExtractor(document)) {
            PageIterator iterator = oe.extract();
            while (iterator.hasNext()) {
                Page page = iterator.next();
                page.getText().forEach(t -> builder.append(t.getText()));
            }
        } catch (Exception e) {
            throw new RuntimeException("parse pdf text error！", e);
        }
        return filterText(builder.toString(), removeSpace);
    }

    public static String parseDoc(File file, boolean removeSpace) {
        String text;
        try (InputStream is = new BufferedInputStream(FileUtil.getInputStream(file))) {
            FileMagic fileMagic = FileMagic.valueOf(is);
            if (fileMagic == FileMagic.OOXML) {
                text = getDocxText(is);
            } else if (fileMagic == FileMagic.OLE2) {
                text = getDocText(is);
            } else {
                throw new RuntimeException("不支持的word文件格式");
            }
            return filterText(text, removeSpace);
        } catch (IOException e) {
            throw new RuntimeException("parse doc text error!", e);
        }
    }

    private static String getDocText(InputStream is) throws IOException {
        try (HWPFDocument document = new HWPFDocument(is)) {
            return document.getDocumentText();
        }
    }

    private static String getDocxText(InputStream is) throws IOException {
        try (XWPFDocument document = new XWPFDocument(is); XWPFWordExtractor extractor = new XWPFWordExtractor(
                document)) {
            return extractor.getText();
        }
    }

    private static String filterText(String text, boolean removeSpace) {
        if (removeSpace && StrUtil.isNotEmpty(text)) {
            return text.replaceAll("\\s*|\r|\n|\t", "");
        }
        return text;
    }
}