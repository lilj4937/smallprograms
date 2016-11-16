package com.smallprograms.route;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * 提取html中的路由信息；
 * 小路由的默认密码.7z
 * 
 * @author LILJ
 *
 */
public class RouteUserPassword {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\LILJ\\Desktop\\小路由的默认密码\\routers");
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for (File f : files) {
				readRouteInfo(f);
				
			}
		}
	}
	
	private static List<RouteVo> readRouteInfo(File file){
		List<RouteVo> dataList = new ArrayList<RouteVo>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = null;
			boolean isstart = false;
			int index = 0;
			RouteVo vo = null;
			while((line = br.readLine()) != null){
				if(line.contains("<tbody>")){
					isstart = true;
				}
				if(isstart){
					Matcher m = Pattern.compile("<td>(.*)</td>").matcher(line);
					while(m.find()){
						if(index == 0){
							vo = new RouteVo();
						}
						index++;
						if(index == 1){
							vo.setManufacturer(replaceStr(m.group(1)));
						}else if(index == 2){
							vo.setModel(replaceStr(m.group(1)));
						}else if(index == 2){
							vo.setProtocol(replaceStr(m.group(1)));
						}else if(index == 2){
							vo.setUsername(replaceStr(m.group(1)));
						}else if(index == 5){
							vo.setPassword(replaceStr(m.group(1)));
							index = 0;
							write(JSONObject.fromObject(vo).toString());
						}
					}
				}
				if(line.contains("</tbody>")){
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fr!=null){
				try {
					fr.close();
					fr = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}
	private static void write(String context){
		if(context == null || "".equals(context.trim())){
			return;
		}
		FileWriter fileWritter = null;
		BufferedWriter bufferWritter = null;
		try {
			File file = new File("E:\\山海诚信\\小路由相关文件\\文档\\userpassword");
			if (!file.exists()) {
				file.createNewFile();
			}
			fileWritter = new FileWriter(file, true);
			bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(context+"\r\n");
			bufferWritter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String replaceStr(String str){
		str = str.replaceAll("<i>", "");
		str = str.replaceAll("</i>", "");
		str = str.replaceAll("<I>", "");
		str = str.replaceAll("</I>", "");
		str = str.replaceAll("<b>", "");
		str = str.replaceAll("</b>", "");
		return str;
	}
}
