package com.searchengine.preprocess.beta.dic;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by RayLew on 2015/5/14.
 * QQ:897929321
 */
public class WordSegHandler {
    public static String getWordSegs(String text) {
        StringBuilder sb = new StringBuilder();
        StringReader sr = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;
        try {
            while ((lex = ik.next()) != null) {
                String matchedWord = lex.getLexemeText();
                sb.append(matchedWord).append("|");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
