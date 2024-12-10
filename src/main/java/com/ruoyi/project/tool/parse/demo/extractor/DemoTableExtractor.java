//package com.ruoyi.project.tool.parse.demo.extractor;
//
//import com.ruoyi.project.tool.parse.domain.Table;
//import com.ruoyi.project.tool.parse.extractor.TableExtractor;
//import com.ruoyi.project.tool.parse.extractor.ListExtractor;
//import com.ruoyi.project.tool.parse.extractor.MapExtractor;
//import lombok.Getter;
//
//import java.util.List;
//
//public class DemoTableExtractor extends TableExtractor {
//
//    private static final String[] REPORT_INFO_CONDITIONS = {"理财产品代码", "理财产品名称", "理财产品登记编码",
//            "产品投资类型", "产品募集方式", "理财产品运作方式", "产品风险评级", "杠杆水平上限",
//            "报告期末理财产品份额总额", "理财产品成立日", "理财产品计划到期日"};
//    private static final String[] ASSETS_INFO_TITLE_CONDITIONS_1 = {"序号", "资产名称", "资产余额", "占产品净资产"};
//
//    @Getter
//    private final ListExtractor listExtractor;
//    @Getter
//    private final MapExtractor mapRule;
//
//    public DemoTableExtractor() {
//        listExtractor = new ListExtractor();
//        mapRule = new MapExtractor(REPORT_INFO_CONDITIONS);
//    }
//
//    @Override
//    public Void extract(List<Table> tableList) {
//        extractPDF_KV(tableList, mapRule);
//        fuzzyMatchingExtractTable(tableList, listExtractor);
//        return null;
//    }
//}
