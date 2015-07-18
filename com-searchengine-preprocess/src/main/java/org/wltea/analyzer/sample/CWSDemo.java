package org.wltea.analyzer.sample;

import com.searchengine.preprocess.alpha.DictSegment;
import com.searchengine.utils.Constant;
import com.searchengine.utils.FileAction;
import com.searchengine.utils.HtmlParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Documented;
import java.util.ArrayList;

/**
 * Created by RayLew on 2015/2/15.
 */
public class CWSDemo {
    public static void main(String[] args) throws IOException, ParseException {
        ikCWS();

    }

    private static void luceneCWS() throws ParseException {
        //Lucene Document的域名
        String fieldName = "text";
        //检索内容
        String text="罗辑思维，目前影响力最大的互联网知识社群，包括微信公众订阅号、知识类脱口秀视频及音频、会员体系、微商城、百度贴吧、微信群等具体互动形式，主要服务于80、90后有爱智求真强烈需求的群体。";

        //实例化IKAnalyzer分词器
        Analyzer analyzer = new IKAnalyzer(true);
        //使用QueryParser查询分析器构造Query对象
        QueryParser qp = new QueryParser(Version.LUCENE_34, fieldName, analyzer);
        qp.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = qp.parse(text);
        System.out.println("Query = " + query);
    }

    private static void ikCWS() throws IOException {
        //1017222828785664
        String text= FileAction.convertFileToString(Constant.WEBPAGE_BASEPATH + "/" + "china.com/news/social/1423840096120.html", Constant.CHARSET_UTF8);
        Document document=Jsoup.parse(text);
        text=document.text();
        StringReader sr = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;
        while ((lex = ik.next()) != null) {
            System.out.print(lex.getLexemeText() + "|");
        }
    }
}
