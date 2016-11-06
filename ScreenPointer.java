package com.qq.util;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 屏幕取点
 * @author Jame
 *
 */
public class ScreenPointer {
	public static void main(String [] args){
		//创建一个窗体
		JFrame jframe = new JFrame();
		//设置尺寸
		jframe.setSize(100,80);
		//设置标题
		jframe.setTitle("屏幕取点工具");
		//设置居中
		jframe.setLocationRelativeTo(null);
		//设置窗体结束就结束进程
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗体永远置顶
		jframe.setAlwaysOnTop(true);
		JLabel jLabel = new JLabel();
		//添加进入窗体
		jframe.add(jLabel);
		//设置显示
		jframe.setVisible(true);
		while(true){
			//获取鼠标的位置信息
			PointerInfo pointer = MouseInfo.getPointerInfo();
			//location 才是我的位置信息 get
			Point point = pointer.getLocation();
			//System.out.println(point.getX()+"^^"+point.getY());
			jLabel.setText("x:"+point.getX()+"   y:"+point.getY());
		}	
	}
}
