package com.hankcs.hanlp.openapi;

import java.util.ArrayList;

/**
 * 评分所需的基础服务
 * @author zhouxuan
 */
public interface Operation {
    /**
     * 计算文本A与文本B的文本相似度
     * @param textOne 主题
     * @param textTwo 文章
     * @return
     */
    public double textSimilarity(String textOne,String textTwo);

    /**
     * 情感分析：极性计算
     * @param text 文本
     * @return 情感极性
     */
    public String sentimentAnalysis(String text);

    /**
     * 文本分类
     * @param text
     * @return 类别
     */
    public String themeClassified(String text);
}
