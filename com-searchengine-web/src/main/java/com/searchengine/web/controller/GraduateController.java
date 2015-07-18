package com.searchengine.web.controller;

import com.searchengine.domain.vo.Result;
import com.searchengine.service.RedisService;
import com.searchengine.utils.CommonUtils;
import com.searchengine.utils.ExcelUtils;
import com.searchengine.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/api/graduate")
@Deprecated
public class GraduateController extends ControllerBase {
    protected final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    /**
     * 存入redis
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveToRedis", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String saveToRedis(HttpServletRequest request) {
        Result<Integer> result = new Result<Integer>();
        int count = 0;
        List<HashMap<String, String>> list = ExcelUtils.getList("F:/数据/毕业生.xls");
        int size = list.size();
        logger.info(size);
        for (int i = 0; i < size; i++) {
            HashMap<String, String> hashMap = list.get(i);
            String key = "gd" + hashMap.get("XM").trim() + hashMap.get("XB").trim() + hashMap.get("ZYMC").trim();
            Object obj = redisService.get(key);
            if (obj == null) {
                redisService.add(key, hashMap.get("ZSBH"));
                count++;
            } else {
                System.out.println(key + ":" + (String) redisService.get(key));
            }
        }
        result.setData(count);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.IntegerType);
    }

    /**
     * 修改Excel的值
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String update(@RequestParam String year) {
        Result<Integer> result = new Result<Integer>();
        int count = 0;
        try {
            List<String> dumpList = getDump("F:/数据/新建文本文档.txt");
            List<HashMap<String, String>> list = ExcelUtils.getList("F:/数据/派遣/湖北师范学院毕业生派遣名单（" + year + "）.xls");
            String[] header = ExcelUtils.getHeaderOfExcel("F:/数据/派遣/湖北师范学院毕业生派遣名单（" + year + "）.xls");
            int rows = list.size();
            for (int i = 0; i < rows; i++) {
                HashMap<String, String> map = list.get(i);
                String key = "gd" + map.get(header[1]) + map.get(header[2]) + map.get(header[4]);
                if (!dumpList.contains(key)) {
                    Object obj = redisService.get(key);
                    if (obj != null) {
                        System.out.println((String) obj);
                        map.put(header[7], (String) obj);
                        count++;
                    }
                } else {
                    map.put(header[7], "有重复");
                }
            }
            ExcelUtils.createExcel(list, "F:/数据/派遣/湖北师范学院毕业生派遣名单（" + year + "）.xls", "F:/数据/new/new湖北师范学院毕业生派遣名单（" + year + "）.xls");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        result.setData(count);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.IntegerType);
    }

    public List<String> getDump(String filePath) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<String>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line = reader.readLine();
            while (line != null && !line.equals("")) {
                list.add(reader.readLine());
                line = reader.readLine();
            }
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("error:method:FileAction-convertFileToString,filePath:" + filePath + ",charset:" + "UTF-8");
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 清除redis的值
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/clearRedis", method = RequestMethod.GET, produces = CommonUtils.MediaTypeJSON)
    @ResponseBody
    public String clearRedis(HttpServletRequest request) {
        Result<Integer> result = new Result<Integer>();
        int count = 0;
        List<HashMap<String, String>> list = ExcelUtils.getList("F:/数据/毕业生.xls");
        int size = list.size();
        logger.info(size);
        for (int i = 0; i < size; i++) {
            HashMap<String, String> hashMap = list.get(i);
            String key = "gd" + hashMap.get("XM").trim() + hashMap.get("XB").trim() + hashMap.get("ZYMC").trim();
            Object obj = redisService.get(key);
            if (obj != null) {
                redisService.remove(key);
                count++;
            }
        }
        result.setData(count);
        result.setTimestamp(new Date());
        return JsonUtils.toJson(result, ResponseResultType.IntegerType);
    }
}
