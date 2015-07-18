package com.searchengine.utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by RayLew on 2015/5/26.
 * QQ:897929321
 */
public class ExcelUtils {

    public static List<HashMap<String, String>> getList(String fileName) {
        List<HashMap<String, String>> list = null;
        try {
            String[] header = getHeaderOfExcel(fileName);
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Workbook book = Workbook.getWorkbook(file);
            Sheet sheet = book.getSheet(0);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            System.out.println(rows + "," + columns);
            list = new LinkedList<HashMap<String, String>>();
            for (int i = 1; i < rows; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Cell[] row = sheet.getRow(i);
                for (int j = 0; j < columns; j++) {
                    map.put(header[j], row[j].getContents().trim());
                }
                list.add(map);
            }
            System.out.println(list.size());
            book.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void createExcel(List<HashMap<String, String>> list, String sourceFileName, String destFileName) {
        try {
            String[] header = getHeaderOfExcel(sourceFileName);
            File file = new File(destFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("Sheet1", 0);
            //设置单元格字体样式
            WritableFont wf_table = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
            WritableCellFormat wcf_table = new WritableCellFormat(wf_table);
            wcf_table.setAlignment(jxl.format.Alignment.CENTRE);
            int rows = list.size();
            int columns = list.get(0).size();
            System.out.println(rows + "," + columns);
            for (int i = 0; i < rows; i++) {
                HashMap<String, String> map = list.get(i);
                for (int j = 0; j < columns; j++) {
                    Label label = new Label(j, i, map.get(header[j]), wcf_table);
                    sheet.addCell(label);
                }
            }
            //写入数据并关闭文件
            book.write();
            book.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 指定读取数据库临时表的列创建存放信息的excel文件
     *
     * @param beginRow    excel开始行
     * @param beginColumn excel开始列
     * @param endColumn   excel结束列
     * @param fileName    生成的excel文件名
     * @param excelHeader 生成的excel文件的表头
     * @param obj         实体类
     * @param list        查询的结果
     */
    public static void createExcel(int beginRow, int beginColumn, int endColumn, String fileName, String[] excelHeader, Object obj, List list) {
        try {
            //新建excel文件
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("查询结果", 0);
            //生成表头
            //定义表头单元格样式
            WritableFont wf_head = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.GREEN); // 定义格式 字体 下划线 斜体 粗体 颜色
            WritableCellFormat wcf_head = new WritableCellFormat(wf_head);
            wcf_head.setAlignment(jxl.format.Alignment.CENTRE);
            for (int k = 0; k < excelHeader.length; k++) {
                Label label = new Label(k, 0, excelHeader[k], wcf_head);
                sheet.addCell(label);
            }
            //利用反射得到实体类的字段信息
            Field[] fields = obj.getClass().getDeclaredFields();
            //设置工作簿的行高和列宽
            for (int i = 0; i < list.size() + beginRow; i++) {
                sheet.setRowView(i, 400);
            }
            for (int i = 0; i < endColumn; i++) {
                sheet.setColumnView(i, 15);
            }
            //设置单元格字体样式
            WritableFont wf_table = new WritableFont(WritableFont.ARIAL, 8,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
            WritableCellFormat wcf_table = new WritableCellFormat(wf_table);
            wcf_table.setAlignment(jxl.format.Alignment.CENTRE);
            //在Label对象的构造子中指名单元格位置是第j列第i行(j,i)
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < endColumn; j++) {
                    if (j == 0) {
                        Label label = new Label(j, i + beginRow, (i + 1) + "", wcf_table);
                        sheet.addCell(label);
                    } else {
                        Object object = list.get(i);
                        Method method = object.getClass().getMethod(get(fields[j].getName()), null); //取得get方法
                        Object o = method.invoke(object, null);//调用实体类的getXXX方法
                        if (fields[j].getType() == int.class) {
                            jxl.write.Number labelN = new jxl.write.Number(j, i + beginRow, Integer.parseInt(o.toString()), wcf_table);
                            sheet.addCell(labelN);
                        } else if (fields[j].getType() == double.class || fields[j].getType() == float.class) {
                            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");
                            jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
                            wcfN.setAlignment(Alignment.CENTRE);
                            jxl.write.Number labelNF = new jxl.write.Number(j, i + beginRow, Double.parseDouble(o.toString()), wcfN);
                            sheet.addCell(labelNF);
                        } else {//非数字类型
                            Label label = null;
                            if (o == null) {
                                label = new Label(j, i + beginRow, "", wcf_table);
                            } else {
                                label = new Label(j, i + beginRow, o.toString(), wcf_table);
                            }
                            sheet.addCell(label);
                        }
                    }
                }
            }
            //写入数据并关闭文件
            book.write();
            book.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 实体的setXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String set(String name) {
        return "set" + (name.charAt(0) + "").toUpperCase() + name.substring(1);//set+变量名的第一个字母大写
    }

    /**
     * 实体的getXXX方法
     *
     * @param name 成员变量名
     * @return
     */
    public static String get(String name) {
        return "get" + (name.charAt(0) + "").toUpperCase() + name.substring(1);//get+变量名的第一个字母大写
    }

    /**
     * 将读出来的行放到实体类中
     *
     * @param name  表头关键词
     * @param type  字段类型
     * @param name  字段名
     * @param value 值
     */
    public static void setBean(Object table, Class<?> type, String name, Object value) throws Exception {
        Method method = table.getClass().getMethod(set(name), type); //取得set方法
        method.invoke(table, value);//调用实体类的setXXX方法
    }

    public static String[] getHeaderOfExcel(String fileName) {
        String[] header = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            Workbook book = Workbook.getWorkbook(file);
            Sheet sheet = book.getSheet(0);
            int column = sheet.getColumns();
            header = new String[column];
            Cell[] row = sheet.getRow(0);
            for (int i = 0; i < column; i++) {
                header[i] = row[i].getContents().trim();
            }
            book.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return header;
    }
}
