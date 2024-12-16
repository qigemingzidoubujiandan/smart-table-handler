package com.ruoyi.project.parse.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author jianwei
 * @date 2023/9/8
 */
public class HutoolExcelUtil {
    /**
     * 方法描述: 自适应宽度(中文支持)
     *
     * @param sheet 页
     * @param size  因为for循环从0开始，size值为 列数-1
     * @return void
     * @author wqf
     */
    public static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            // 确保列宽不会超过 255
            if (columnWidth > 255) {
                columnWidth = 255;
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
}
