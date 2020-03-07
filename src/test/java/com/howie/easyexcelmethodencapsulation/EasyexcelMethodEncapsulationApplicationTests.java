package com.howie.easyexcelmethodencapsulation;

import com.alibaba.excel.util.StringUtils;
import com.howie.easyexcelmethodencapsulation.excel.ExcelUtil;
import com.howie.easyexcelmethodencapsulation.test.ImportInfo;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EasyexcelMethodEncapsulationApplicationTests {

	@Test
	public void contextLoads() throws InvalidFormatException {

		String excelPath="D:\\fhl\\6.22~6.28(钉钉).xlsx";
		System.out.println("获取文件:"+excelPath);
		File excelFile=new File(excelPath);
		FileInputStream fileInputStream= null;
		try {
			fileInputStream = new FileInputStream(excelFile);
			//要读取的sheet 这里从1开始
			int sheetNo=3;
			//要读取的行 从0开始
			int headLineNum=3;
			List<ImportInfo> list=ExcelUtil.getRowsByInputStream(fileInputStream,ImportInfo.class,sheetNo,headLineNum);

			list=list.stream().filter(vo-> {
				Boolean filter=StringUtils.isEmpty(vo.getStandTime());
				return !filter;
			}).collect(Collectors.toList());
			list=list.stream().filter(vo-> {
				Boolean filter=vo.getStandTime().contains("17:30");
				return filter;
			}).collect(Collectors.toList());
			list=list.stream().filter(vo-> {
				LocalDate standTime = vo.getStandTimeLocal().toLocalDate();
				Boolean filter=vo.getCardTimeLocal().isAfter(LocalDateTime.of(standTime, LocalTime.of(20, 30)));
				filter=filter&&vo.getResult().contains("正常");
				return filter;
			}).collect(Collectors.toList());


			Map<String, Long> map = new HashMap<>();

			//去掉重复打卡的记录
			List<ImportInfo> list2= Lists.newArrayList();
			HashSet<Object> replied=new HashSet<>();
			list2.addAll(list);
			list2.removeIf(c -> replied.add(Arrays.asList(c.getName(),c.getDate())));
			list.removeAll(list2);
			System.out.println(list.size());

			//按姓名分组统计
			map=list.stream().collect(
					Collectors.groupingBy(ImportInfo::getName,Collectors.counting())
			);
			System.out.println(map);
            String sourceFilePath="D:\\fhl\\LYZH-研发内部学习月活动情况统计表-20190629-0.xlsx";
            String destFilePath="D:\\fhl\\LYZH-研发内部学习月活动情况统计表-20190629.xlsx";
            //这里是第二个sheet
            int sheetAt=1;
            repalceValue(sourceFilePath,sheetAt,map,destFilePath);
		} catch (FileNotFoundException e) {
			System.out.println("获取文件异常");
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
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