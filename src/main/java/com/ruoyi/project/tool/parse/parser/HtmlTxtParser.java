package com.ruoyi.project.tool.parse.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

@Slf4j
@AllArgsConstructor
public class HtmlTxtParser extends AbstractTextParser<Document> {

    @Override
    public String parse(Document dataSource) {
        return dataSource.text().replaceAll("\\s*|\r|\n|\t", "");
    }
}
