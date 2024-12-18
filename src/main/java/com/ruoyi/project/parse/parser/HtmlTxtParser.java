package com.ruoyi.project.parse.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class HtmlTxtParser extends AbstractTextParser<String> {

    @Override
    public String parse(String dataSource) {
        Document document = null;
        try {
            document = Jsoup.parse(new File(dataSource), "UTF-8");
        } catch (IOException e) {
            log.error("解析失败");
            return "";
        }
        return document.text().replaceAll("\\s*|\r|\n|\t", "");
    }
}
