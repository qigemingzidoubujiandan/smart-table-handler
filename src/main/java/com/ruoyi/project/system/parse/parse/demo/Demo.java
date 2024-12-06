//package com.ruoyi.project.system.parse.parse.demo;
//
//import com.ruoyi.project.system.parse.parse.demo.extractor.DemoTableExtractor;
//import com.ruoyi.project.system.parse.parse.demo.extractor.DemoTextExtractor;
//import com.ruoyi.project.system.parse.parse.parser.PDFTxtParser;
//import com.ruoyi.project.system.parse.parse.parser.SpirePDFTableParser;
//import com.ruoyi.project.system.parse.parse.parser.TabulaPDFTableParser;
//import com.ruoyi.project.system.parse.parse.parser.manage.CombineParser;
//import com.ruoyi.project.system.parse.parse.parser.manage.TableParser;
//import com.ruoyi.project.system.parse.parse.parser.manage.TextParser;
//
//import java.util.List;
//
//public class Demo {
//
//    /**
//     * 支持多个解析器，单个抽取器
//     */
//    private static final TableParser<DemoTableExtractor> TABLE_PARSER = TableParser.<DemoTableExtractor>builder()
//            .parser(new TabulaPDFTableParser())
//            .parser(new SpirePDFTableParser())
//            .extractor(new DemoTableExtractor()).build();
//
//    /**
//     * 支持单个解析器，单个抽取器
//     */
//    private static final TextParser<DemoTextExtractor.DemoExtractDTO> TEXT_PARSER = TextParser.<DemoExtractDTO>builder()
//            .parser(new PDFTxtParser())
//            .extractor(new DemoTextExtractor())
//            .build();
//
//    /**
//     * 组合解析器。支持表格解析器+文本解析器
//     */
//    private static final CombineParser<DemoTableExtractor, DemoTextExtractor.DemoExtractDTO> COMBIN_PARSER = CombineParser.<DemoTableExtractor, DemoExtractDTO>builder()
//            .parser(new TabulaPDFTableParser())
//            .parser(new SpirePDFTableParser())
//            .parser(new PDFTxtParser())
//            .extractor(new DemoTableExtractor())
//            .extractor(new DemoTextExtractor())
//            .build();
//
//
//    public static void main(String[] args) {
//        String dataSource = "/Users/maohaitao/Desktop/2b1ad406-7d16-49bd-8368-d834419c97c3.pdf";
//
//        System.out.println("========test table parser========");
//        List<Table> parseTable = TABLE_PARSER.parse(dataSource).getTableList();
//        DemoTableExtractor tableExtractRet = TABLE_PARSER.parse(dataSource).extract().getTableExtractRet();
//        System.out.println(parseTable);
//        System.out.println(tableExtractRet.getListRule().getParsedResult());
//        System.out.println(tableExtractRet.getMapRule().getParsedResult());
//
//        System.out.println("========test text parser========");
//        DemoExtractDTO extract = TEXT_PARSER.parse(dataSource).extract();
//        System.out.println(extract);
//
//        System.out.println("========test combin parser========");
//        Result<DemoTableExtractor, DemoExtractDTO> combin = COMBIN_PARSER.parse(dataSource).extract().combine();
//        System.out.println(combin.getTextExtractRet());
//        System.out.println(combin.getTableExtractRet().getListRule());
//    }
//}
