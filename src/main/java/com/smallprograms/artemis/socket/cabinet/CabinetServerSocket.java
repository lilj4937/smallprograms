package com.smallprograms.artemis.socket.cabinet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class CabinetServerSocket {
	private int PORT = 5001; // 监听端口 5001
	private int MAXSIZE = 100;// 最多接收100个字节
	private ServerSocket serverSocket = null;
	
	public CabinetServerSocket(){}
	
	public CabinetServerSocket(int port, int maxSize){
		this.PORT = port;
		this.MAXSIZE = maxSize;
	}
	
	public static void main(String[] args) {
		CabinetServerSocket server = new CabinetServerSocket();
		server.init();
	}
	public void init() {
		try {
			serverSocket = new ServerSocket(PORT);
			// 等待请求,此方法会一直阻塞,直到获得请求才往下走
			while (true) {
				Socket socket = serverSocket.accept();
				Client client = new Client(socket,MAXSIZE); // 创建客户端处理线程对象
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
	private Socket socket = null; // 保存客户端Socket对象
	private int maxSize;

	public Client(Socket socket,int maxSize) {
		this.socket = socket;
		this.maxSize = maxSize;
	}

	public void run() {
		// 打印出客户端数据
		try {
			InputStream in = socket.getInputStream();
			byte[] temp = new byte[maxSize];
			int len = in.read(temp,0,maxSize);
			String message = bytesToHexString(temp);
			if("4C 4D FE".equals(message.toUpperCase())){
				byte[] data = new byte[11];
			    data[0] = (byte) 0x53;
			    data[1] = (byte) 0x0C;
			    data[2] = (byte) 0x19;
			    data[3] = (byte) 0x23;
			    data[4] = (byte) 0x00;
			    data[5] = (byte) 0x00;
			    data[6] = (byte) 0x00;
			    data[7] = (byte) 0x00;
			    data[8] = (byte) 0x00;
			    data[9] = (byte) 0x80;
			    data[10] = (byte) 0x80;
			    data[11] = (byte) 0x48;
			}
			
			
			
			socket.close();
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
	            stringBuilder.append(0+" ");   
	        }   
	        stringBuilder.append(hv+" ");   
	    }   
	    return stringBuilder.toString().trim();   
	}
}

class CabinetClientSocket{
	private int port;
	private byte[] data;
	public CabinetClientSocket(int port,byte[] data){
		this.port = port;
		this.data = data;
	}
	
	public void sendMessage() throws UnknownHostException, IOException{
		Socket s = new Socket("127.0.0.1", this.port); //创建一个Socket对象，连接IP地址为192.168.24.177的服务器的5566端口  
	    DataOutputStream dos = new DataOutputStream(s.getOutputStream()); //获取Socket对象的输出流，并且在外边包一层DataOutputStream管道，方便输出数据  
	    dos.write(data);
	    dos.flush(); //确保所有数据都已经输出  
	    dos.close(); //关闭输出流  
	    s.close(); //关闭Socket连接  
	}
}