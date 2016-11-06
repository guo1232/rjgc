package com.tz.screen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * Java远程桌面系统客户端(学生端)
 * @author LLZ
 * @version 2.0
 *
 */


public class Client {

	//Java入口
	public static void main(String[] args) {
		
		String input = JOptionPane.showInputDialog("请输入要连接的服务器(端口号)：","58.61.246.64:10000");
		
		try{
			//先要获取服务器主机，从0开始到：部分的信息
			String host = input.substring(0,input.indexOf(":"));
			
			//获取端口号,获取：后面的东西
			String post = input.substring(input.indexOf(":")+1);
			System.out.println(host + ":" + post);
			
			//连接服务器
			//(主机,端口号)
			//通过Socket获取数据的输入流
			Socket client = new Socket(host,Integer.parseInt(post));
			
			//数据输入流
			DataInputStream dis = new DataInputStream(client.getInputStream());
			
			//创建面板
			JFrame jframe = new JFrame();
			//窗体关闭要关闭进程
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			jframe.setTitle("远程桌面监控系统客户端");
			//创建窗口
			jframe.setSize(1024,768);
			//设置窗体出现位置
			jframe.setLocation(350,0);
			
			//读取服务器(教师端)屏幕分辨率
			double height = dis.readDouble();
			double width = dis.readDouble();
			
			Dimension dimensionServer = new Dimension((int)height,(int)width);
			
			//设置
			jframe.setSize(dimensionServer);
			//将服务端的照片作为背景
			JLabel backImage = new JLabel();
			JPanel panel = new JPanel();
			
			//需要制作滚动条
			JScrollPane scrollPane = new JScrollPane(panel);
			panel.setLayout(new FlowLayout());
			
			//add背景图片
			panel.add(backImage);
			//add滚动条
			jframe.add(scrollPane);
			//设置窗体置顶
			jframe.setAlwaysOnTop(true);
			//设置窗体可见
			jframe.setVisible(true);
			
			//循环读取图片
			while(true){
				//先读取数据大小
				int len = dis.readInt();
				//获取的参数放置到一个数组里面
				byte[] imageData = new byte[len];
				
				//读取图片的数据流
				dis.readFully(imageData);
				
				//完整地读取数据的二进制数组
				ImageIcon image = new ImageIcon(imageData);
				backImage.setIcon(image);
				
				//重新绘制面板
				jframe.repaint();
				
			}
			
		}catch(Exception e){
			e.getStackTrace();
		}

	}

}
