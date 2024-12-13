package com.ruoyi.project.parse.parser;

import cn.hutool.core.io.FileUtil;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.project.parse.domain.FileTypeEnum;
import com.ruoyi.project.parse.domain.ParseTypeEnum;

/**
 * @author chenl
 */
public class ParserFactory {

    public static IParser createParser(FileTypeEnum fileType, ParseTypeEnum parserType) {
        switch (fileType) {
            case PDF:
                if (ParseTypeEnum.TABLE.equals(parserType)) {
                    return new SpirePDFTableParser(); // 可以根据需求选择不同的表格解析器
                } else if (ParseTypeEnum.TEXT.equals(parserType)) {
                    return new PDFTextParser(); // 或者其他文本解析器
                }
                break;
            case WORD:
                if (ParseTypeEnum.TABLE.equals(parserType)) {
                    return new WordTableParser();
                } else if (ParseTypeEnum.TEXT.equals(parserType)) {
                    return new WordTxtParser();
                }
                break;
            case HTML:
                if (ParseTypeEnum.TABLE.equals(parserType)) {
                    return new HtmlTableParser();
                } else if (ParseTypeEnum.TEXT.equals(parserType)) {
                    return new HtmlTxtParser();
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        throw new IllegalArgumentException("Unsupported parser type: " + parserType);
    }

    /**
     * 根据文件后缀名返回对应的 FileTypeEnum。
     */
    public static FileTypeEnum getFileTypeByExtension(String filePath) {
        String extension = FileUtil.getSuffix(filePath).toLowerCase();
        switch (extension) {
            case "pdf":
                return FileTypeEnum.PDF;
            case "doc":
            case "docx":
                return FileTypeEnum.WORD;
            case "html":
            case "htm":
                return FileTypeEnum.HTML;
            default:
                throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
    }

    /**
     * 根据文件路径和解析类型创建解析器。
     */
    public static IParser createParserByFilePath(String filePath, ParseTypeEnum parserType) {
        FileTypeEnum fileType = getFileTypeByExtension(filePath);
        return createParser(fileType, parserType);
    }
}
