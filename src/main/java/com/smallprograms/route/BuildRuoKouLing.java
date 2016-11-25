package com.smallprograms.route;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成弱密码
 * 
 * @author LILJ
 *
 */
public class BuildRuoKouLing {
	private static List<String> USERNAMELIST = new ArrayList<String>();
	private static List<String> PASSWORDLIST = new ArrayList<String>();

	static {
		USERNAMELIST.add("webmaster");
		USERNAMELIST.add("admin");
		USERNAMELIST.add("administrator");
		USERNAMELIST.add("1");
		USERNAMELIST.add("123");
		USERNAMELIST.add("a");
		USERNAMELIST.add("root");
		USERNAMELIST.add("user");
		USERNAMELIST.add("web");
		USERNAMELIST.add("test");
		USERNAMELIST.add("anonymous");
		USERNAMELIST.add("guest");

		//%username%替换成用户名
		//%null%是空字符串
		PASSWORDLIST.add("%null%");
		PASSWORDLIST.add("%username%");
		PASSWORDLIST.add("123456");
		PASSWORDLIST.add("12345678");
		PASSWORDLIST.add("123456789");
		PASSWORDLIST.add("111111");
		PASSWORDLIST.add("11111111");
		PASSWORDLIST.add("000000");
		PASSWORDLIST.add("88888888");
		PASSWORDLIST.add("123123");
		PASSWORDLIST.add("666666");
		PASSWORDLIST.add("00000000");
		PASSWORDLIST.add("1234567");
		PASSWORDLIST.add("888888");
		PASSWORDLIST.add("asdf");
		PASSWORDLIST.add("asdfg");
		PASSWORDLIST.add("asdfgh");
		PASSWORDLIST.add("qwer");
		PASSWORDLIST.add("qwert");
		PASSWORDLIST.add("qwerty");
		PASSWORDLIST.add("zxcv");
		PASSWORDLIST.add("zxcvb");
		PASSWORDLIST.add("zxcvbn");
		PASSWORDLIST.add("1");
		PASSWORDLIST.add("111");
		PASSWORDLIST.add("123");
		PASSWORDLIST.add("1234");
		PASSWORDLIST.add("654321");
		PASSWORDLIST.add("a");
		PASSWORDLIST.add("aaa");
		PASSWORDLIST.add("!@#$");
		PASSWORDLIST.add("!@#$%");
		PASSWORDLIST.add("!@#$%^");
		PASSWORDLIST.add("!@#$%^&");
		PASSWORDLIST.add("!@#$%^&*");
		PASSWORDLIST.add("%username%123");
		PASSWORDLIST.add("%username%1234");
		PASSWORDLIST.add("%username%!@#$");
	}

	public static void main(String[] args) {
		String sqlmodel = "INSERT INTO \"main\".\"弱密码\" VALUES (?1, '?2', '?3');";
		int id = 1;
		for (String username : USERNAMELIST) {
			for (String password : PASSWORDLIST) {
				String sql = buildSql(sqlmodel, id++,username, password);
				write(sql);
			}
		}
	}
	private static void write(String context){
		if(context == null || "".equals(context.trim())){
			return;
		}
		FileWriter fileWritter = null;
		BufferedWriter bufferWritter = null;
		try {
			File file = new File("E:\\山海诚信\\小路由相关文件\\文档\\ruomima");
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
	
	private static String buildSql(String sqlmodel, Integer id, String username, String password){
		sqlmodel = sqlmodel.replace("?1", id.toString());
		sqlmodel = sqlmodel.replace("?2", username);
		sqlmodel = sqlmodel.replace("?3", replaceStr(password, username));
		return sqlmodel;
	}
	
	private static String replaceStr(String str,String username){
		str = str.replaceAll("%username%", username);
		str = str.replaceAll("%null%", "");
		return str;
	}
}
