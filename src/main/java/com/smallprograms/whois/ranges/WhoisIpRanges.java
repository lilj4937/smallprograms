package com.smallprograms.whois.ranges;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 检测不在给定ip段的ip段
 * 
 * @author LILJ
 *
 */
public class WhoisIpRanges {
	private static File readingFile = null;
	private static Integer IPSUM = 0;
	private static boolean ISOVER = false;
	private static FileReader fileReader = null;
	private static BufferedReader bufferReader = null;
	
	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<String>();
		String line = null;
		int count = 0;
		while ((line = readIpPort("C:\\Users\\LILJ\\Desktop\\2016-10-27.17.27.11.ipwhois")) != null) {
			count++;
			if("".equals(line.trim())){
				continue;
			}
			if("isover".equals(line.trim())){
				break;
			}
			String[] arr = line.split(",");
			String ip1 = arr[0];
			String ip2 = arr[1];
			
			/*long ip1_l = ipToLong(ip1);
			long ip2_l = ipToLong(ip2);*/
			long ip1_l = Long.valueOf(ip1);
			long ip2_l = Long.valueOf(ip2);
			
			if((ip2_l - ip1_l) >= 16581375){
				continue;
			}
			
			list.add(line);
		}
		
		System.out.println(list.size());
		
		Collections.sort(list, new Comparator<String>(){

			@Override
			public int compare(String o1, String o2) {
				String ip1 = o1.split(",")[0];
				String ip2 = o2.split(",")[0];
				long l1 = Long.valueOf(ip1);
				long l2 = Long.valueOf(ip2);
				if(l1==l2){
                    return 0;
                }else if(l1>l2){
                    return 1;
                }else{
                    return -1;
                }
			}
			
		});
		
		String ip = "1.0.0.0";//检测大约16777216的ip段
		long ip_l = ipToLong(ip);
		
		for (String range : list) {
			if("".equals(range.trim())){
				continue;
			}
			if("isover".equals(range.trim())){
				break;
			}
			
			String[] arr = range.split(",");
			String ip1 = arr[0];
			String ip2 = arr[1];
			
			/*long ip1_l = ipToLong(ip1);
			long ip2_l = ipToLong(ip2);*/
			long ip1_l = Long.valueOf(ip1);
			long ip2_l = Long.valueOf(ip2);
			
			if((ip2_l - ip1_l) >= 16581375){
				continue;
			}
			
			if(ip_l < ip1_l){
				writeResult(ip_l+"-"+(ip1_l-1), "d:\\whois.ranges");
				ip_l = ip2_l+1;
				ip = longToIp(ip_l);
			}else if(ip_l <= ip2_l){
				ip_l = ip2_l+1;
				ip = longToIp(ip_l);
			}
		}
		System.out.println("count="+count);
	}
	
	public static long ipToLong(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}
		
		return result;
	}
	public static String longToIp(long ip) {

		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}
	
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
