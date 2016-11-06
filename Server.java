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
 * javaԶ�������ط����(��ʦ��)
 * @author LLZ
 * @version 2.0
 *
 */

public class Server {

	public static void main(String[] args) {
		
		try{
			//��������������10000
			//ServerSocket������ʵ�ַ��������ӣ��������׽��ֵȴ�����ͨ�����紫�룬�����ڸ�����ִ��ĳЩ����
			//Ȼ������������߷��ؽ��
			ServerSocket ss = new ServerSocket(10000);
			
			while(true){
				System.out.println("�ȴ�����......");
				//�ȴ����ӣ����������ܵ����׽��ֵ�����
				Socket client = ss.accept();
				
				System.out.println("���ӳɹ�......");
				
				//ͨ��socket��ȡ�����,ת���������
				OutputStream os = client.getOutputStream();
				
				//ת���ɶ����Ƶ�������
				DataOutputStream doc = new DataOutputStream(os);
				
				//�����߳�
				//Ҫ�����߳���ScreenThread
				//doc���������
				ScreenThread screenThread = new ScreenThread(doc);
				
				//�����߳�
				screenThread.start();
			}
	
		}catch(Exception e){
			e.getStackTrace();
			System.out.println("����ʧ��......");
		}
	}
	
}

//����Ļ���̵߳ķ�ʽ,����Ļ���Ͻ�ȡ��ͼƬ���ϵشӽ���˷��͵�ѧ����
//Thread��:�����Ѿ�ʵ�ֵĽӿ�
class ScreenThread extends Thread{
	
	//��������������ϰ�ͼƬ���
	private DataOutputStream dataOut;
	
	//���캯��
	public ScreenThread(DataOutputStream dataOut){
		this.dataOut = dataOut;
	}
	
	//�����߳�
	//�����߳���Ļ��ʼ����
	public void run(){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		
		try{
			
			//��ȡ��Ļ�ķֱ���
			//��ȡ�������(��ʦ��)���͵��ͻ���(ѧ����)
			//�ļ������
			dataOut.writeDouble(dm.getHeight());
			dataOut.writeDouble(dm.getWidth());
			dataOut.flush();
			
			//���������С
			//Rectangleָ������ռ��һ������ͨ������ռ��е�Rectangle�������Ϸ��ĵ�(x,y)����Ⱥ͸߶ȿ��Զ����������
			Rectangle rec = new Rectangle(dm);
			
			//ͨ�������˻�ȡ����
			Robot robot = new Robot();
			
			
			//����ѭ��
			while(true){
				//ͨ�������˽�ȡ����ͼƬ
				BufferedImage bufImg = robot.createScreenCapture(rec);
				//�ö��������������洢ת�����ͼƬ,����ѹ��
				//ѹ����Ƭ�ڴ��С,���������Զ����Ƶ��ص�������
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				//ѹ��
				//JPEGImageEncoder:���ڰ�ͼƬת���ɶ����ƽ���ѹ��
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
				encoder.encode(bufImg);
				
				//������ͼƬ��ȡת���ɶ���������
				byte[] data = baos.toByteArray();
				
				//��ͼƬת��ɶ����������Ժ�Ҫ������ĳ��ȷ��͵��ͻ���(ѧ����)
				dataOut.writeInt(data.length);
				dataOut.write(data);
				dataOut.flush();
				
				//�ӳ�20����
				//Thread.sleep(20);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
