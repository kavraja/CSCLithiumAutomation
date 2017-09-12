package com.cisco.csc.genericLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.net.URL;
import java.net.URLEncoder;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelUtil {

	public ExcelUtil() {
		// TODO Auto-generated constructor stub
	}

	
	//via string path
	public static String[][] fetchData(URL URLpath, String sheetName) throws BiffException, IOException {
		//System.out.println("Fetching data fom : " + URLpath.getPath());
		Workbook workbook = Workbook.getWorkbook(new File(URLpath.getPath()));
		Sheet sheet = workbook.getSheet(sheetName);
		return fetchDataFromSheet(sheet);
	}

	//via resource url path
	public static String[][] fetchData(String URLpath, String sheetName) throws BiffException, IOException {
		//System.out.println("Fetching data fom : " + URLpath);
		Workbook workbook = Workbook.getWorkbook(new File(URLpath));
		Sheet sheet = workbook.getSheet(sheetName);
		return fetchDataFromSheet(sheet);
	}
	
	// Read Properties File
	public static String[][] fetchDataFromSheet(Sheet sheet) throws BiffException, IOException {
		
		
		int startRow = sheet.findCell("start").getRow() + 1;
		//System.out.println("startrow : " + startRow);

		int stopRow = sheet.findCell("end").getRow() - 1;
		//System.out.println("stoprow : " + stopRow);

		int startColl = sheet.findCell("start" + "").getColumn() + 1;
		//System.out.println("startColl : " + startColl);

		int stopColl = sheet.findCell("end").getColumn() - 1;
		//System.out.println("stopcoll : " + stopColl);

		//System.out.println("start row/col : " + startRow + "/" + startColl);
		//System.out.println("end row/col : " + stopRow + "/" + stopColl);
		
		

		String[][] tabValues = new String[stopRow - startRow + 1][stopColl - startColl + 1];
		int i1 = 0, j1 = 0;

		for (int i = startRow; i <= stopRow; i++, i1++) {
			j1 = 0;
			for (int j = startColl; j <= stopColl; j++, j1++) {
				// //System.out.println("i: "+i+" j:"+j);
				tabValues[i1][j1] = sheet.getCell(j, i).getContents();

			}

		}

		//System.out.println("Read File...");
		return tabValues;
	}



	public String readProp(String key) {

		File file = new File("TestData/Conf.properties");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String value = prop.getProperty(key);
		//System.out.println(value);
		return value;
	}

}
