package com.howie.easyexcelmethodencapsulation.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 导入 Excel 时使用的映射实体类，Excel 模型
 * @Date 2018-06-05
 * @Time 21:41
 */
public class ImportInfo extends BaseRowModel {
    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 5)
    private String date;

    @ExcelProperty(index = 6)
    private String standTime;

    @ExcelProperty(index = 7)
    private String cardTime;

    @ExcelProperty(index = 8)
    private String result;



    private LocalDateTime standTimeLocal;
    private LocalDateTime cardTimeLocal;


    public LocalDateTime getStandTimeLocal() {
        return standTimeLocal;
    }

    public LocalDateTime getCardTimeLocal() {
        return cardTimeLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStandTime() {
        return standTime;
    }

    public void setStandTime(String standTime) {
        this.standTime = standTime;
        String s = this.standTime  +":00";
        LocalDateTime beginDateTime = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.standTimeLocal = beginDateTime;
    }

    public String getCardTime() {
        return cardTime;
    }

    public void setCardTime(String cardTime) {
        this.cardTime = cardTime;
        String s = this.cardTime  +":00";
        LocalDateTime beginDateTime = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.cardTimeLocal = beginDateTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"date\":\"")
                .append(date).append('\"');
        sb.append(",\"standTime\":\"")
                .append(standTime).append('\"');
        sb.append(",\"cardTime\":\"")
                .append(cardTime).append('\"');
        sb.append(",\"result\":\"")
                .append(result).append('\"');
        sb.append('}');
        return sb.toString();
    }
}