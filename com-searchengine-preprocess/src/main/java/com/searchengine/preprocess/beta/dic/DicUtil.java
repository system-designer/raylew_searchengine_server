package com.searchengine.preprocess.beta.dic;

import com.searchengine.domain.Word;
import com.searchengine.domain.dic.WordCategory;
import com.searchengine.domain.dic.WordType;
import com.searchengine.service.WordService;
import com.searchengine.utils.StringUtils;
import org.wltea.analyzer.cfg.DefaultConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by RayLew on 2015/2/15.
 */
public class DicUtil {
    /**
     * 将特殊词导入数据库
     * @param wordService
     */
    public void importMainWordDicToDB(WordService wordService){
        DefaultConfig defaultConfig= (DefaultConfig) DefaultConfig.getInstance();
        //读取主词典文件
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(defaultConfig.getMainDictionary());
        if(is == null){
            throw new RuntimeException("Main Dictionary not found!!!");
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && StringUtils.isNotEmpty(theWord.trim())) {
                    Word word=new Word();
                    word.setType(WordType.WORDTYPE_MAIN);
                    word.setCategory(WordCategory.WORDCATEGORY_CN);
                    word.setName(theWord.trim().toLowerCase());
                    wordService.add(word);
                }
            } while (theWord != null);

        } catch (Exception ex) {
            System.err.println("error:method:DicUtil-importDicToDB,Main Dictionary loading exception.");
            ex.printStackTrace();
        }finally{
            try {
                if(is != null){
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将停用词导入数据库
     * @param wordService
     */
    public void importStopWordDicToDB(WordService wordService){
        DefaultConfig defaultConfig= (DefaultConfig) DefaultConfig.getInstance();
        //读取主词典文件
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(defaultConfig.getMainDictionary());
        if(is == null){
            throw new RuntimeException("Main Dictionary not found!!!");
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && StringUtils.isNotEmpty(theWord.trim())) {
                    Word word=new Word();
                    word.setType(WordType.WORDTYPE_MAIN);
                    word.setCategory(WordCategory.WORDCATEGORY_CN);
                    word.setName(theWord.trim().toLowerCase());
                    wordService.add(word);
                }
            } while (theWord != null);

        } catch (Exception ex) {
            System.err.println("error:method:DicUtil-importDicToDB,Main Dictionary loading exception.");
            ex.printStackTrace();
        }finally{
            try {
                if(is != null){
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
