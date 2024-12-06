package com.ruoyi.project.system.parse.parse.parser.model;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Data
@AllArgsConstructor
public class Area {

    /**
     * 顶部定位坐标（页面最顶部截止到所需区间顶部之间的宽度 ）
     */
    private float top;
    /**
     * 底部定位坐标（页面最底部截止到所需区间底部之间的宽度 ）
     */
    private float bottom;
    /**
     * 左侧定位坐标（页面最左侧截止到所需区间左侧之间的宽度 ）
     */
    private float left;
    /**
     * 右侧定位坐标（页面最右侧截止到所需区间右侧之间的宽度 ）
     */
    private float right;

    public Rectangle convertToRectangle(PDPage page) {
        PDRectangle cropBox = page.getCropBox();
        float width = NumberUtil.toBigDecimal(cropBox.getWidth()).subtract(NumberUtil.toBigDecimal(this.right))
                .floatValue();
        float height = NumberUtil.toBigDecimal(cropBox.getHeight()).subtract(NumberUtil.toBigDecimal(this.bottom))
                .floatValue();
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        return new Rectangle(this.top, this.left, width, height);
    }

    public Rectangle convertToRectangle(Page page) {
        float width = NumberUtil.toBigDecimal(page.width).subtract(NumberUtil.toBigDecimal(this.right))
                .floatValue();
        float height = NumberUtil.toBigDecimal(page.height).subtract(NumberUtil.toBigDecimal(this.bottom))
                .floatValue();
        if (width < 0) {
            width = 0;
        }
        if (height < 0) {
            height = 0;
        }
        return new Rectangle(this.top, this.left, width, height);
    }

    /**
     * y1,x1,y2,x2
     */
    public Optional<String> convertToRectangleStr(String pdfPath) {

        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            ObjectExtractor oe = new ObjectExtractor(document);
            Page page = oe.extract(1);

            float width = NumberUtil.toBigDecimal(page.width).subtract(NumberUtil.toBigDecimal(this.right))
                    .floatValue();
            float height = NumberUtil.toBigDecimal(page.height).subtract(NumberUtil.toBigDecimal(this.bottom))
                    .floatValue();
            if (width < 0) {
                width = 0;
            }
            if (height < 0) {
                height = 0;
            }
            String rst = this.top + "," + this.left + "," + height + "," + width;
            return Optional.of(rst);
        } catch (IOException e) {
            log.error("convertToRectangleStr error! pdfPath:{}", pdfPath, e);
            return Optional.empty();
        }
    }

}
