package com.ruoyi.project.tool.parse.demo.extractor;

import com.ruoyi.project.tool.parse.anno.Reg;
import com.ruoyi.project.tool.parse.extractor.AbstractAnnoTextExtractor;
import lombok.Data;

public class DemoTextExtractor extends AbstractAnnoTextExtractor<DemoTextExtractor.DemoExtractDTO> {

    @Override
    public DemoExtractDTO extract(String text) {
        return super.extract(text);
    }

    @Data
    public static class DemoExtractDTO {

        @Reg("本报告期自(.*?)起至(.*?)止")
        @Reg("本报告期自(.*?)日起至(.*?)止")
        private String reportStartDate;

        @Reg(value = "本报告期自(.*?)日起至(.*?)止", index = 2)
        private String reportEndDate;

        @Reg("资产净值为(.*?)[，。]")
        private String assetsNetWorth;
    }
}
