package com.howie.easyexcelmethodencapsulation;

import com.alibaba.excel.util.StringUtils;
import com.howie.easyexcelmethodencapsulation.excel.ExcelUtil;
import com.howie.easyexcelmethodencapsulation.test.ImportInfo;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class EasyexcelMethodEncapsulationApplicationTests {

	@Test
	public void contextLoads() {

		String excelPath="/Users/luodengxiong/Downloads/dingtalk5.xlsx";
		System.out.println("获取文件:"+excelPath);
		File excelFile=new File(excelPath);
		FileInputStream fileInputStream= null;
		try {
			fileInputStream = new FileInputStream(excelFile);
			//要读取的sheet 从1开始
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
		} catch (FileNotFoundException e) {
			System.out.println("获取文件异常");
			e.printStackTrace();
		}
	}

}