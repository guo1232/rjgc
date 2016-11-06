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
 * JavaԶ������ϵͳ�ͻ���(ѧ����)
 * @author LLZ
 * @version 2.0
 *
 */


public class Client {

	//Java���
	public static void main(String[] args) {
		
		String input = JOptionPane.showInputDialog("������Ҫ���ӵķ�����(�˿ں�)��","58.61.246.64:10000");
		
		try{
			//��Ҫ��ȡ��������������0��ʼ�������ֵ���Ϣ
			String host = input.substring(0,input.indexOf(":"));
			
			//��ȡ�˿ں�,��ȡ������Ķ���
			String post = input.substring(input.indexOf(":")+1);
			System.out.println(host + ":" + post);
			
			//���ӷ�����
			//(����,�˿ں�)
			//ͨ��Socket��ȡ���ݵ�������
			Socket client = new Socket(host,Integer.parseInt(post));
			
			//����������
			DataInputStream dis = new DataInputStream(client.getInputStream());
			
			//�������
			JFrame jframe = new JFrame();
			//����ر�Ҫ�رս���
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			jframe.setTitle("Զ��������ϵͳ�ͻ���");
			//��������
			jframe.setSize(1024,768);
			//���ô������λ��
			jframe.setLocation(350,0);
			
			//��ȡ������(��ʦ��)��Ļ�ֱ���
			double height = dis.readDouble();
			double width = dis.readDouble();
			
			Dimension dimensionServer = new Dimension((int)height,(int)width);
			
			//����
			jframe.setSize(dimensionServer);
			//������˵���Ƭ��Ϊ����
			JLabel backImage = new JLabel();
			JPanel panel = new JPanel();
			
			//��Ҫ����������
			JScrollPane scrollPane = new JScrollPane(panel);
			panel.setLayout(new FlowLayout());
			
			//add����ͼƬ
			panel.add(backImage);
			//add������
			jframe.add(scrollPane);
			//���ô����ö�
			jframe.setAlwaysOnTop(true);
			//���ô���ɼ�
			jframe.setVisible(true);
			
			//ѭ����ȡͼƬ
			while(true){
				//�ȶ�ȡ���ݴ�С
				int len = dis.readInt();
				//��ȡ�Ĳ������õ�һ����������
				byte[] imageData = new byte[len];
				
				//��ȡͼƬ��������
				dis.readFully(imageData);
				
				//�����ض�ȡ���ݵĶ���������
				ImageIcon image = new ImageIcon(imageData);
				backImage.setIcon(image);
				
				//���»������
				jframe.repaint();
				
			}
			
		}catch(Exception e){
			e.getStackTrace();
		}

	}

}
