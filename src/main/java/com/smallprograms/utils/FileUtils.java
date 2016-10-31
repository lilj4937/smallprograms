package com.smallprograms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
	
	private static File readingFile = null;
	private static Integer IPSUM = 0;
	private static boolean ISOVER = false;
	
	
	private static FileReader fileReader = null;
	private static BufferedReader bufferReader = null;
	
	public synchronized static String readIpPort(String filePath) throws IOException{
		if(ISOVER){
			return "isover";
		}
		IPSUM++;
        String reslut = null;
        if(bufferReader == null){
        	readingFile = new File(filePath);
			if (!readingFile.exists()) {
				return null;
			}
			fileReader = new FileReader(readingFile);
			bufferReader = new BufferedReader(fileReader);
		}
        reslut = bufferReader.readLine();

		if(reslut == null){
			ISOVER = true;
			close();
		}
		return reslut;
	}
	
	private static void close(){
		try {
			
			if(bufferReader != null){
				bufferReader.close();
				bufferReader = null;
			}
			
			if(fileReader != null){
				fileReader.close();
				fileReader = null;
			}
			
			if(readingFile != null && readingFile.exists()){
				//readingFile.delete();
				readingFile = null;
			}
		} catch (IOException e) {
		}
	}

	private static FileWriter fileWritter = null;
	private static BufferedWriter bufferWritter = null;
	
	public static synchronized void writeResult(String json,String filePath){
		if(json == null || "".equals(json.trim())){
			return;
		}
		try {
				
			if(bufferWritter!=null){
				bufferWritter.close();
				bufferWritter = null;
			}
			if(fileWritter != null){
				fileWritter.close();
				fileWritter = null;
			}
			
			if(bufferWritter == null){
				File file = new File(filePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				fileWritter = new FileWriter(file, true);
				bufferWritter = new BufferedWriter(fileWritter);
			}
			bufferWritter.write(json+"\r\n");
			bufferWritter.flush();
		} catch (IOException e) {
		}
	}
	
	
}
