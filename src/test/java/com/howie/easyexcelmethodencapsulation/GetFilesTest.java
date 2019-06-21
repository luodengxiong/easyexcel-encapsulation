package com.howie.easyexcelmethodencapsulation;

import org.junit.Test;

import java.io.File;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * @desc: 读取文件
 **/
public class GetFilesTest {

    @Test
    public void getFile(){
        String path="D:\\temp";
        File tfsFileRoot=new File(path);
        traverseFolder(tfsFileRoot);
    }

    public void traverseFolder(File tfsFileRoot){
        File[] tfsFiles=tfsFileRoot.listFiles();
        for (int i=0;i<tfsFiles.length;i++){
            File tfsFile=tfsFiles[i];
            if (!tfsFile.isHidden()) {
                if (tfsFile.isDirectory()) {
                    traverseFolder(tfsFile);
                } else {
                    Instant instant = Instant.ofEpochMilli(tfsFile.lastModified());
                    LocalDateTime fileLastModified=LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
                    LocalDate inputDate = LocalDate.now();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //本周开始时间
                    TemporalAdjuster FIRST_OF_WEEK =
                            TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
                    if (fileLastModified.isAfter(LocalDateTime.of(inputDate.with(FIRST_OF_WEEK),LocalTime.of(18, 30)))) {
                        System.out.println("文档地址: " + tfsFile.getPath());
                    }
                }
            }
        }
    }


}
