package com.ruoyi.project.system.parse.parse.extractor;

public class TableExtractAdoptor extends AbstractTableExtractor{

    @Override
    public int expectParseRowSize() {
        return 0;
    }

    @Override
    public String[] condition() {
        return new String[0];
    }

    @Override
    public boolean parsed() {
        return false;
    }

    @Override
    public void fillMatchedData(Object o) {

    }

    @Override
    public Object matchedData() {
        return null;
    }
}
