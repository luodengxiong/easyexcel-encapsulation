package com.howie.easyexcelmethodencapsulation;

import com.howie.easyexcelmethodencapsulation.excel.TimeUtil;
import com.howie.easyexcelmethodencapsulation.vo.FileVO;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.File;
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @date: 2019/6/21 8:54:35
 **/
public class GetFilesTest {
    List<FileVO> fileVOList=Lists.newArrayList();

    @Test
    public void getFile(){
        String path="D:\\TEMP";
        File tfsFileRoot=new File(path);


        traverseFolder(tfsFileRoot);

        LocalDate inputDate = LocalDate.now();
        //本周开始时间
        TemporalAdjuster FIRST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        fileVOList.sort(Comparator.comparing(FileVO::getDate));
        fileVOList= fileVOList.stream().filter(vo-> {
            Boolean filter = false;
            if(vo.getDate()!="") {
                LocalDate fileDate = TimeUtil.parseDate(vo.getDate(), TimeUtil.TimeFormat.SHORT_DATE_PATTERN_NONE);
                LocalDateTime fileDateTime = LocalDateTime.of(fileDate, LocalTime.of(23, 30));
                filter = fileDateTime.isAfter(LocalDateTime.of(inputDate.with(FIRST_OF_WEEK), LocalTime.of(18, 30)));
            }
            return filter;
        }).collect(Collectors.toList());
        for (int i=0;i<fileVOList.size();i++){
            System.out.println("\n");
            System.out.println(String.format("%03d",i) + ":"+ fileVOList.get(i).getDate() +": " +fileVOList.get(i).getTeamName()+" "+fileVOList.get(i).getFilePath());
        }
    }

    public void traverseFolder(File tfsFileRoot){
        File[] tfsFiles=tfsFileRoot.listFiles();

        Pattern datePattern=Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
        for (int i=0;i<tfsFiles.length;i++){
            File tfsFile=tfsFiles[i];
            String filePath=tfsFile.getPath();
            if (filePath.contains("A")
                    || filePath.contains("B")
                    || filePath.contains("C")
                    || filePath.contains("D")
                    || filePath.contains("E")
                    || filePath.contains("F")
                    ) {
                if (tfsFile.isDirectory()) {
                    traverseFolder(tfsFile);
                } else {
                    Boolean filterFiles=!filePath.contains("F1")
                            && !filePath.contains("2018")
                            && !filePath.contains("F2")
                            && !filePath.contains("F3")
                            && !filePath.contains("F4");

                    Instant instant = Instant.ofEpochMilli(tfsFile.lastModified());
                    LocalDateTime fileLastModified=LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
                    LocalDate inputDate = LocalDate.now();
                    //本周开始时间
                    TemporalAdjuster FIRST_OF_WEEK =
                            TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
                    if (filterFiles
                            && fileLastModified.isAfter(LocalDateTime.of(inputDate.with(FIRST_OF_WEEK),LocalTime.of(18, 30)))) {
                        String teamName="T1";

                        String filePathFull=tfsFile.getPath();
                        Matcher matcher=datePattern.matcher(filePathFull);
                        String date = "";
                        if(matcher.find()) {
                            date = matcher.group(1);
                        }
                        if (date==""){
                            System.out.println(teamName+ ": " + tfsFile.getPath());
                        }else {
                            FileVO fileVO = new FileVO(teamName, filePathFull, date);
                            fileVOList.add(fileVO);
                        }
                        //System.out.println("fileVOList:"+fileVOList.size());
                        //System.out.println(teamName+ ": " + tfsFile.getPath());
                    }
                }
            }
        }

    }


}
