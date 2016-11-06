package com.qq.util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * ������:������Ļ���
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
		//Java������һ���������set,��ȡһ���������get
		//���ñ���
		jFrame.setTitle("����A�β��԰�");
		//���þ���
		jFrame.setLocationRelativeTo(null);
		//���ô����С
		jFrame.setSize(850,850);
		//��ǩ
		JLabel jLabel = new JLabel();
		jFrame.add(jLabel);
		//����ر�
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		//���ô���ɼ�
		jFrame.setVisible(true);
		//���������˶���
		Robot robot = new Robot();
		//����x,y,width,height
		Rectangle rec = new Rectangle(568,10,750,780);
		//���ϵؽ�ȡͼƬ
		while(true){
			//Buffered�ڴ���
			BufferedImage buf = robot.createScreenCapture(rec);
			//shift ����ͼƬ
			jLabel.setIcon(new ImageIcon(buf));
			//����50����,���߳�����һ��
			//Thread.sleep(50);
		}

	}
}
