package com.tz.screen;

import com.sun.image.codec.jpeg.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.image.codec.jpeg.*;

/**
 * 
 * java远程桌面监控服务端(教师端)
 * @author LLZ
 * @version 2.0
 *
 */

public class Server {

	public static void main(String[] args) {
		
		try{
			//建立服务器监听10000
			//ServerSocket类用于实现服务器连接，服务器套接字等待请求通过网络传入，它基于该请求执行某些操作
			//然后可能向请求者返回结果
			ServerSocket ss = new ServerSocket(10000);
			
			while(true){
				System.out.println("等待连接......");
				//等待连接，倾听并接受到此套接字的连接
				Socket client = ss.accept();
				
				System.out.println("连接成功......");
				
				//通过socket获取输出流,转换成输出流
				OutputStream os = client.getOutputStream();
				
				//转换成二进制的数据流
				DataOutputStream doc = new DataOutputStream(os);
				
				//启动线程
				//要定义线程类ScreenThread
				//doc数据输出流
				ScreenThread screenThread = new ScreenThread(doc);
				
				//启动线程
				screenThread.start();
			}
	
		}catch(Exception e){
			e.getStackTrace();
			System.out.println("请求失败......");
		}
	}
	
}

//把屏幕以线程的方式,把屏幕不断截取的图片不断地从教书端发送到学生端
//Thread类:所有已经实现的接口
class ScreenThread extends Thread{
	
	//数据输出流，不断把图片输出
	private DataOutputStream dataOut;
	
	//构造函数
	public ScreenThread(DataOutputStream dataOut){
		this.dataOut = dataOut;
	}
	
	//调用线程
	//启动线程屏幕开始传输
	public void run(){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		
		try{
			
			//截取屏幕的分辨率
			//获取到服务端(教师端)发送到客户端(学生端)
			//文件输出流
			dataOut.writeDouble(dm.getHeight());
			dataOut.writeDouble(dm.getWidth());
			dataOut.flush();
			
			//矩形区域大小
			//Rectangle指定坐标空间的一个区域，通过坐标空间中的Rectangle对象左上方的点(x,y)、宽度和高度可以定义这个区域
			Rectangle rec = new Rectangle(dm);
			
			//通过机器人获取数据
			Robot robot = new Robot();
			
			
			//不断循环
			while(true){
				//通过机器人截取本地图片
				BufferedImage bufImg = robot.createScreenCapture(rec);
				//用二进制数据流来存储转换后的图片,进行压缩
				//压缩照片内存大小,有利于提高远程视频监控的流畅度
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				//压缩
				//JPEGImageEncoder:用于把图片转换成二进制进行压缩
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
				encoder.encode(bufImg);
				
				//将本地图片截取转换成二进制数组
				byte[] data = baos.toByteArray();
				
				//将图片转变成二进制数组以后，要将数组的长度发送到客户端(学生端)
				dataOut.writeInt(data.length);
				dataOut.write(data);
				dataOut.flush();
				
				//延迟20毫秒
				//Thread.sleep(20);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
