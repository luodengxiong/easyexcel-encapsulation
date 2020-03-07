package com.howie.easyexcelmethodencapsulation;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.google.common.collect.Sets;
import com.howie.easyexcelmethodencapsulation.excel.ExcelUtil;
import com.howie.easyexcelmethodencapsulation.test.ImportInfo;
import com.howie.easyexcelmethodencapsulation.vo.People;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.util.Lists;
import org.joda.time.Interval;
import org.junit.Test;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EasyexcelCountTests {


    private LinkedHashMap<String, People> peoples = new LinkedHashMap<String, People>();

	@Test
	public void contextLoads() throws InvalidFormatException {

		String excelPath="D:\\fhl\\联运知慧研发部1-12月考勤.xlsx";
		System.out.println("获取文件:"+excelPath);
		File excelFile=new File(excelPath);
		FileInputStream fileInputStream= null;
		try {
			fileInputStream = new FileInputStream(excelFile);

			//要读取的sheet 这里从1开始
			int sheetNo=12;
			//要读取的行 从0开始
			int headLineNum=3;

            dataMerge(fileInputStream,sheetNo,headLineNum);
            //System.out.println(peoples);
            for (Map.Entry<String, People> m : peoples.entrySet()) {
                //System.out.println(peoples);
                System.out.println(m.getValue());
            }

		} catch (FileNotFoundException e) {
			System.out.println("获取文件异常");
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dataMerge(FileInputStream fileInputStream,int sheetNo,int headLineNum){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.now();
        String today = dateFormatter.format(time);

        List<Object> data = EasyExcelFactory.read(fileInputStream, new com.alibaba.excel.metadata.Sheet(sheetNo, headLineNum));
        Map<String, Object> map = new HashMap<>();
        Set ADD_SCORE_RECORD_TRADE_TYPE_SET = Sets.newHashSet("罗登雄","王山林","王鑫","安培勇","张耀辛","赵哲成","熊刚");
        //Set ADD_SCORE_RECORD_TRADE_TYPE_SET = Sets.newHashSet("罗登雄");
        for (Object ob:data) {
            if (Objects.nonNull(ob)){
                ArrayList<String> list=( ArrayList<String>)ob;
                String people=list.get(0);
                if (ADD_SCORE_RECORD_TRADE_TYPE_SET.contains(people)){
                    People pdata= peoples.get(people);
                    if (Objects.isNull(pdata)){
                        pdata= new People();
                    }
                    Integer times=0;
                    Integer hours=0;
                    if (Objects.nonNull(pdata.getTimes())){
                        times=pdata.getTimes();
                    }
                    if (Objects.nonNull(pdata.getHours())){
                        hours=pdata.getHours();
                    }
                    if (Objects.isNull(pdata.getName())){
                        pdata.setName(people);
                    }
                    if (Objects.isNull(pdata.getMonth())){
                        pdata.setMonth(""+sheetNo);
                    }
                    int listSize=list.size();
                    for (int im=6;im<listSize;im++){
                        String timeStr=list.get(im);
                        if (Objects.nonNull(timeStr) && timeStr.contains("\n")){

                            //计算时长
                            String[] timeStrings=timeStr.split("\n");
                            String timeStart="";
                            String timeEnd="";
                            timeStart=timeStrings[0].trim();
                            timeEnd=timeStrings[timeStrings.length-1].trim();
                            LocalDateTime workEnd = LocalDateTime.parse(today+ " "+timeEnd+"",timeFormatter);
                            LocalDate standWorkEnd = LocalDate.now();
                            LocalDateTime standWorkEndTime = LocalDateTime.parse(today+ " 18:30",timeFormatter);
                            Boolean filter=workEnd.isAfter(LocalDateTime.of(standWorkEnd, LocalTime.of(18, 30)));
                            if (workEnd.isBefore(LocalDateTime.of(standWorkEnd, LocalTime.of(8, 30)))){
                                //次日凌晨
                                workEnd=workEnd.plusDays(1);
                                filter=true;
                            }

//                            if (timeStrings.length==2){
//                                timeEnd=timeStrings[1];
//                            }else {
//                                timeEnd=timeStrings[timeStrings.length];
//                            }
                            if (filter){
                                //System.out.println(" timeStart: " + timeStart +" timeEnd:" +timeEnd);
                                //System.out.println(" standWorkEndTime: " + timeFormatter.format(standWorkEndTime) +" workEnd:" +timeFormatter.format(workEnd));
                                times++;//次数加1
                                //long hour= ChronoUnit.HOURS.between(standWorkEndTime,workEnd);
                                Duration duration=Duration.between(standWorkEndTime,workEnd);
                                hours+=Integer.valueOf((int)duration.toHours());
                                //System.out.println("hour: "+duration.toHours()+" timeStart: " + timeStart +" timeEnd:" +timeEnd);
                            }
                        }
                    }
                    pdata.setTimes(times);
                    pdata.setHours(hours);
                    //System.out.println(list);
                    if (times.intValue()>0 && hours.intValue()>0) {
                        peoples.put(people, pdata);
                    }
                }

            }
        }
    }


	public void print(List<Object> datas){
		int i=0;
		for (Object ob:datas) {
			System.out.println(i++);
			System.out.println(ob);
		}
	}

    /**
     * 根据文件模板替换内容
     * @param sourceFilePath
     * @param sheetAt
     * @param map
     * @param destFilePath
     * @throws IOException
     * @throws InvalidFormatException
     */
	public void repalceValue(String sourceFilePath,int sheetAt, Map<String, Long> map,String destFilePath) throws IOException, InvalidFormatException {
	    //WorkbookFactory.create 方法兼容Excel新旧版本
        File excelFile=new File(sourceFilePath);
        Workbook wb=WorkbookFactory.create(excelFile);
        Sheet sheet = wb.getSheetAt(sheetAt) ;//获取第 sheetAt 个 sheet里的内容
        if (Objects.nonNull(map)) {
            int rowNum= sheet.getLastRowNum();//该sheet页里最多有几行内容
            for(int i=3;i<rowNum;i++) {//从第4行开始，循环每一行
                Row row=sheet.getRow(i);
                //第3列是姓名 第9列是需要替换的数值
                Cell cell = row.getCell((short)2);
                String personName=cell.getStringCellValue();

                for (String peopleName:map.keySet()){
                    if(peopleName.equals(personName) || (peopleName.endsWith("1") && peopleName.contains(personName)  )){
                        Cell cellReplace = row.getCell((short)8);
                        Integer times= map.get(peopleName).intValue();
						System.out.println("personName:"+personName + " 次数 "+ times);
                        cellReplace.setCellValue(times);
                    }
                }
            }
        }
        //需要更新公式内容
        sheet.setForceFormulaRecalculation(true);
        //替换内容后保存新的文件
        OutputStream outputStream = new FileOutputStream(destFilePath);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
        wb.close();
    }


}