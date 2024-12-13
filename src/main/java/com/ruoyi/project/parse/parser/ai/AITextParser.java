package com.ruoyi.project.parse.parser.ai;

import com.google.common.collect.Lists;
import com.ruoyi.project.parse.parser.IParser;

import java.util.List;
import java.util.stream.Collectors;

public class AITextParser implements IParser<AIRequest, AIResponse> {

    List<IParser<AIRequest, AIResponse>> aiParser = Lists.newArrayList(new SparkParser());
//    private static volatile boolean running = false;
    /**
     * 解析
     */
    @Override
    public AIResponse parse(AIRequest t) {
//        synchronized (AITextParser.class) {
//            while (running) {
//                ThreadUtil.sleep(200);
//            }
//        }
//        try{
            List<AIResponse> collect = aiParser.stream().map(p -> p.parse(t)).collect(Collectors.toList());
            if (verify(collect)) {
                return collect.get(0);
            }
            throw new RuntimeException("not equal");
//        }
//        finally {
//            running = false;
//        }
    }

    private boolean verify(List<AIResponse> collect) {
        if (collect.size() <= 1) return true;
        if (collect.size() != aiParser.size()) return false;

        for (int i = 0; i < collect.size() - 1; i++) {
            AIResponse t1 = collect.get(i);
            AIResponse t2 = collect.get(i + 1);
            if (!t1.equals(t2)) {
                return false;
            }
        }
        return true;
    }
}
