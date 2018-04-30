package com.hankcs.hanlp.openapi.impl;

import com.hankcs.hanlp.openapi.Operation;
import org.junit.Test;

import static org.junit.Assert.*;

public class OperationImplTest {

    Operation operation=new OperationImpl();
    String text1="你还好吗，我的朋友";
    String text2="小船儿飘荡";
    @Test
    public void textSimilarity() {
        System.out.println(operation.textSimilarity(text1,text2));
    }

    @Test
    public void sentimentAnalysis() {
        System.out.println(operation.sentimentAnalysis(text1));
    }

    @Test
    public void themeClassified() {
        System.out.println(operation.themeClassified(text2));
    }
}