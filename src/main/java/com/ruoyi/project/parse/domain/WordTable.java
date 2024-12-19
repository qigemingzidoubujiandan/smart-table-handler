package com.ruoyi.project.parse.domain;

import com.ruoyi.project.parse.domain.Enum.FileTypeEnum;
import lombok.Data;

import java.util.List;

@SuppressWarnings("rawtypes")
@Data
public class WordTable extends Table {

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.WORD;
    }
}
