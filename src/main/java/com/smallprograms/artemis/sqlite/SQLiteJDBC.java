package com.smallprograms.artemis.sqlite;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SQLiteJDBC {
	INSTENCE;

	private static Connection CONN = null;
	public static Integer BASEDATAMAXID = 0;// 基础数据Id自大值
	public static String USERPASSWORD = null;// 弱口令密码

	public static void main(String[] args) throws Exception {
		initConn();
		ResultSet rs = null;
		try {
			Statement stat = CONN.createStatement();
			rs = stat.executeQuery("select * from conversation_entries"); // 查询数据
			while(rs.next()){
				System.out.println(getContent(rs.getBinaryStream("data")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		}
	}

	private static Connection initConn() throws Exception {
		if (CONN != null) {
			return CONN;
		}

		Class.forName("org.sqlite.JDBC");
		// 建立一个数据库名route.db的连接，如果不存在就在当前目录下创建之
		// 红色部分路径要求全小写，大写会报错
		CONN = DriverManager
				.getConnection("jdbc:sqlite://C:/Users/LILJ/Desktop/592567431-47.db");

		return CONN;
	}
	private static String getContent(InputStream is) throws IOException {
		if (is == null)
			return null;
		StringBuffer sb = new StringBuffer();
		int size = is.available();
		byte[] bytes = new byte[size];
		try {
			while ((is.read(bytes)) != -1) {
				for (byte b : bytes) {
					System.out.print(b+",");
				}
				System.out.println();
				if (bytes[10] == 106) {// 匹配英文字节
					sb.append(new String(bytes, 12, bytes[11]));
				} else if (bytes[10] == 66) {// 匹配中文
					
					String str = new String(bytes, 12, Math.abs(bytes[11] * 2));
					System.out.println(str);
					if (str.contains("88")) {
						Pattern p = Pattern.compile("(.*)88");
						Matcher m = p.matcher(str);
						if (m.find()) {
							str = m.group(1);
						}
					}
					sb.append(str);
				} else if(bytes[10] == 105){//匹配图片链接
					int num = bytes[12] + bytes[13]; //链接字节长度
					for(int i=0;i<bytes.length;i++){
						if(bytes[i] == 90){
							sb.append(new String(bytes, i+1, num));
						}
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
