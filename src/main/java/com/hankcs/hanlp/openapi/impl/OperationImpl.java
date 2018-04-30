package com.hankcs.hanlp.openapi.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.openapi.Operation;

import java.io.File;
import java.io.IOException;

public class OperationImpl implements Operation {
    /**
     * 词库
     */
    private static final String TRAIN_FILE_NAME = HanLP.LOCAL_PATH+"data/test/搜狗文本分类语料库已分词.txt";
    /**
     * 词向量保存模型
     */
    private static final String MODEL_FILE_NAME = HanLP.LOCAL_PATH+"data/test/word2vec.txt";
    /**
     * 中文情感挖掘语料-ChnSentiCorp 谭松波
     */
    private static final String CORPUS_FOLDER = HanLP.LOCAL_PATH+"data/test/ChnSentiCorp情感分析酒店评论";
    /**
     * 搜狗文本分类语料库5个类目，每个类目下1000篇文章，共计5000篇文章
     */
    public static final String CLASS_PATH = HanLP.LOCAL_PATH+"data/test/搜狗文本分类语料库迷你版";
    /**
     * 模型保存路径
     */
    public static final String MODEL_PATH = HanLP.LOCAL_PATH+"data/test/classification-model.ser";

    @Override
    public double textSimilarity(String textOne, String textTwo)  {
        double similarity =0;
        try {
            WordVectorModel wordVectorModel = trainOrLoadWord2VecModel();
            DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
            similarity=(double)docVectorModel.similarity(textOne,textTwo);

        }catch (IOException e){
            System.out.println("相似度计算异常"+e);
            System.exit(1);
        }
        return similarity;

    }


    @Override
    public String sentimentAnalysis(String text) {
        // 创建分类器，更高级的功能请参考IClassifier的接口定义
        IClassifier classifier = new NaiveBayesClassifier();
        String pos =null;
        try {
            // 训练后的模型支持持久化，下次就不必训练了
            classifier.train(CORPUS_FOLDER);
            pos = classifier.classify(text);
        }catch (IOException e){
            System.out.println("情感分类异常："+e);
            System.exit(1);
        }
        return pos;
    }


    @Override
    public String themeClassified(String text) {

        String category =null;
        try {
            IClassifier classifier = new NaiveBayesClassifier(trainOrLoadClassModel());
            category = classifier.classify(text);
        }catch (IOException e){
            System.out.println("文本分类异常："+e);
            System.exit(1);
        }
        return category;
    }

    /**
     * 训练词向量分类器
     * @return
     * @throws IOException
     */
    static WordVectorModel trainOrLoadWord2VecModel() throws IOException
    {
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
                System.exit(1);
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }

        return loadModel();
    }

    static WordVectorModel loadModel() throws IOException
    {
        return new WordVectorModel(MODEL_FILE_NAME);
    }


    /**
     * 训练文本分类分类器
     */

    private static NaiveBayesModel trainOrLoadClassModel() throws IOException
    {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH);
        if (model != null) return model;

        File corpusFolder = new File(CLASS_PATH);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
        {
            System.err.println("没有文本分类语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式与语料下载：" +
                "https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90");
            System.exit(1);
        }
        // 创建分类器，更高级的功能请参考IClassifier的接口定义
        IClassifier classifier = new NaiveBayesClassifier();
        // 训练后的模型支持持久化，下次就不必训练了
        classifier.train(CLASS_PATH);
        model = (NaiveBayesModel) classifier.getModel();
        IOUtil.saveObjectTo(model, MODEL_PATH);
        return model;
    }

}
