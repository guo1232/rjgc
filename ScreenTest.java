package com.qq.util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 类描述:本地屏幕监控
 * @author LLZ
 * @throws InterruptedException
 */
public class ScreenTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception{
		JFrame jFrame = new JFrame();
		//Java中设置一个类的属性set,获取一个类的属性get
		//设置标题
		jFrame.setTitle("哆啦A梦测试版");
		//设置居中
		jFrame.setLocationRelativeTo(null);
		//设置窗体大小
		jFrame.setSize(850,850);
		//标签
		JLabel jLabel = new JLabel();
		jFrame.add(jLabel);
		//窗体关闭
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		//设置窗体可见
		jFrame.setVisible(true);
		//创建机器人对象
		Robot robot = new Robot();
		//代表x,y,width,height
		Rectangle rec = new Rectangle(568,10,750,780);
		//不断地截取图片
		while(true){
			//Buffered内存中
			BufferedImage buf = robot.createScreenCapture(rec);
			//shift 设置图片
			jLabel.setIcon(new ImageIcon(buf));
			//休眠50毫秒,让线程休眠一下
			//Thread.sleep(50);
		}

	}
}
