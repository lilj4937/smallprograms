package com.smallprograms.lingdian.tunshixingkong;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smallprograms.utils.FileUtils;


public class Main {
	private static String TUNSHIXINGKONGURL = "http://www.00ksw.com/html/0/8/";
	private static String outpath = "D:\\tunshixingkong\\";
	//view-source:http://www.00ksw.com/html/0/8/
	public static void main(String[] args) throws Exception {
		String context = doGet(TUNSHIXINGKONGURL,null);
		buildZhangjieLink(context);
	}
	/**
	 * 匹配章节的链接
	 * @param html
	 * @throws Exception
	 */
	private static void buildZhangjieLink(String html) throws Exception{
		Pattern pattern = Pattern.compile("<dd><a href=\"(.*?)\">");
		Matcher matcher = pattern.matcher(html);
		int index = 0;
		while (matcher.find()) {
			index ++;
			//匹配提取章节url
			String url = TUNSHIXINGKONGURL+matcher.group(1);
			build(url, index);
		}
	}
	
	private static void build(String url,int index){
		try {
			//获取章节内容
			String context = doGet(url,null);
			//从章节内容中提取章节名字
			String zhangjieName = buildZhangjieName(context);
			if(zhangjieName == null){
				build(url,index);
				return;
			}
			System.out.println(zhangjieName);
			zhangjieName = index+zhangjieName;
			//从章节内容中提取章节内容
			String zhangjieContext = buildZhangjieContext(context);
			//输出
			FileUtils.writeResult(zhangjieContext, outpath+zhangjieName);
		} catch (Exception e) {
			build(url,index);
		}
	}
	
	
	/**
	 * 提取章节名称
	 * @param html
	 */
	private static String buildZhangjieName(String html){
		Pattern pattern = Pattern.compile("<h1>(.*?)</h1>");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	/**
	 * 匹配章节内容
	 * @param html
	 * @return
	 */
	private static String buildZhangjieContext(String html){
		Pattern pattern = Pattern.compile("<div id=\"content\">(.*?)</div>");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	public static String doGet(String url, Map<String, String> headers)
			throws Exception {

		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setConnectTimeout(8000);
		httpURLConnection.setReadTimeout(8000);
		httpURLConnection.setInstanceFollowRedirects(false); 
		httpURLConnection.setRequestMethod("GET");
		
		/*
		 * httpURLConnection.setRequestProperty("Accept-Charset",
		 * ENCODING_UTF_8); httpURLConnection.setRequestProperty("Content-Type",
		 * "application/x-www-form-urlencoded");
		 */

		if (headers != null) {
			for (String key : headers.keySet()) {
				httpURLConnection.setRequestProperty(key, headers.get(key));
			}
		}

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,"gbk");
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				if(tempLine.contains("<br />") || tempLine.contains("<br/>")){
					resultBuffer.append("\r\n");
				}
				resultBuffer.append(tempLine.replaceAll("&nbsp;", " ").replaceAll("<br />", " ").replaceAll("<br/>", " "));
			}

		} catch (Exception e) {
			return null;
		} finally {
			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}
		return resultBuffer.toString();
	}
}
