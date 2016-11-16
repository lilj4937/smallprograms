package com.smallprograms.artemis.socket.cabinet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 监听5001端口 充当机柜
 * 
 * @author LILJ
 *
 */
public class CabinetServerSocket {
	private int PORT = 5000; //服务端监听端口 5000
	private ServerSocket serverSocket = null;
	
	public CabinetServerSocket(){}
	
	public CabinetServerSocket(int port, int maxSize){
		this.PORT = port;
	}
	
	public static void main(String[] args) {
		CabinetServerSocket server = new CabinetServerSocket();
		server.init();
	}
	public void init() {
		try {
			serverSocket = new ServerSocket(PORT);
			int count = 0;
			// 等待请求,此方法会一直阻塞,直到获得请求才往下走
			while (true) {
				count ++;
				Socket socket = serverSocket.accept();
				System.out.println("count:"+count);
				Client client = new Client(socket); // 创建客户端处理线程对象
				Thread thead = new Thread(client); // 创建客户端处理线程
				thead.start(); // 启动线程
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

// 客户端处理线程类(实现Runnable接口)
class Client implements Runnable {
	public static byte[] DATAINFO = new byte[13];
	private Socket socket = null; // 保存客户端Socket对象

	public Client(Socket socket) {
		this.socket = socket;
	}
	
	static{
		DATAINFO[0] = (byte) 0x53;
		DATAINFO[1] = (byte) 0x0C;
		DATAINFO[2] = (byte) 0x19;
		DATAINFO[3] = (byte) 0x23;
		DATAINFO[4] = (byte) 0x00;
		DATAINFO[5] = (byte) 0x00;
		DATAINFO[6] = (byte) 0x00;
		DATAINFO[7] = (byte) 0x00;
		DATAINFO[8] = (byte) 0x00;
		DATAINFO[9] = (byte) 0x00;
		DATAINFO[10] = (byte) 0x86;
		DATAINFO[11] = (byte) 0x80;
		DATAINFO[12] = (byte) 0x48;
	}

	public void run() {
		// 打印出客户端数据
		try {
			System.out.println("==================================");
			InputStream in = socket.getInputStream();
			byte[] temp = new byte[1024];
			in.read(temp,0,1024);
			String message = new String(temp);
			System.out.println("接收到的信息："+message);
			boolean b = false;
			if("LMS\\xfe".equals(message.trim())){//获取信息
				b = true;
			}else if("LCDFS\\xfe".equals(message.trim())){//开启前门
				byte bt = DATAINFO[4];
				if(bt == 1){
					DATAINFO[4] = (byte) 0x11;
				}else{
					DATAINFO[4] = (byte) 0x10;
				}
			}else if("LCDBS\\xfe".equals(message.trim())){//开启后门
				byte bt = DATAINFO[4];
				if(bt == 10){
					DATAINFO[4] = (byte) 0x11;
				}else{
					DATAINFO[4] = (byte) 0x01;
				}
			}
			socket.close();
			CabinetClientSocket client = null;
			if(b){
				client = new CabinetClientSocket(5001, DATAINFO);
			}else{
				client = new CabinetClientSocket(5001, "SRL+0XFE");
			}
			client.sendMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv+" ");   
	    }   
	    return stringBuilder.toString().trim();   
	}
}

class CabinetClientSocket{
	private int port;
	private byte[] data;
	private String message;
	public CabinetClientSocket(int port,byte[] data){
		this.port = port;
		this.data = data;
	}
	
	public CabinetClientSocket(int port,String message){
		this.port = port;
		this.message = message;
	}
	
	public void sendMessage() throws UnknownHostException, IOException{
		Socket s = new Socket("192.168.1.89", this.port); //创建一个Socket对象，连接IP地址为192.168.24.177的服务器的5566端口  
	    DataOutputStream dos = new DataOutputStream(s.getOutputStream()); //获取Socket对象的输出流，并且在外边包一层DataOutputStream管道，方便输出数据  
	    if(data != null){
		    dos.write(data);
			System.out.print("发送的信息 data:");
		    for (byte bt : data) {
				System.out.print(" "+bt);
			}
		    System.out.println();
	    }
	    
	    if(message != null){
		    dos.write(message.getBytes());
			System.out.println("发送的信息 message:"+message);
	    }
	    dos.flush(); //确保所有数据都已经输出  
	    dos.close(); //关闭输出流  
	    dos = null;
	    s.close(); //关闭Socket连接  
	    s = null;
	}
}