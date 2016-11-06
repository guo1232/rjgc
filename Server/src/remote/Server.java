package remote;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ������:�����ƶ�
 * @author Admin
 * @throws IOException
 */
public class Server {
	
	public static void main(String args[]) throws Exception {
		while(true){
			//��������������10000
			//ServerSocket������ʵ�ַ��������ӣ��������׽��ֵȴ�����ͨ�����紫�룬�����ڸ�����ִ��ĳЩ����
			//Ȼ������������߷��ؽ��
			
			    //�����߳�
				//Ҫ�����߳���ScreenThread
				//socket���������
				ObjectInputStream objectIn;
			
				ServerSocket serverSocket = new ServerSocket(10000);
				System.out.println("��������10000�˿ڼ���...");
				System.out.println("�ȴ�����...");
				//accpet����
				Socket socket = serverSocket.accept();
				System.out.println("���ӳɹ�����"+socket.getInetAddress());
				//ʵ�������������
				DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
				//�����߳�(ʵ����ͼƬ����߳�)
				ImageThread imageThread = new ImageThread(dataOut);
				//����start()����
				imageThread.start();
				objectIn = new ObjectInputStream(socket.getInputStream());
				EventThread eventThread = new EventThread(objectIn);
				eventThread.start();
			} 
			
		}
	
}

/**
 * ������:��ȡ�¼��ķ���
 */
class EventThread extends Thread{
	public ObjectInputStream objectIn;
	//ģ�����
	private Robot robot;
	public EventThread(ObjectInputStream in){
		this.objectIn = in;
	}
	public void run(){
		try{
			robot = new Robot();
			InputEvent event;
			while(true){
				event = (InputEvent)objectIn.readObject();
				//�����¼�
				actionEvent(event);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void actionEvent(InputEvent e){
		//�����¼�
		if(e instanceof KeyEvent){
			KeyEvent key = (KeyEvent)e;
			//��ȡ�����̵��¼����
			int type = key.getID();
			if(type == Event.KEY_PRESS){
				robot.keyPress(key.getKeyCode());
			}else if(type == Event.KEY_RELEASE){
				robot.keyRelease(key.getKeyCode());
			}
		}else if(e instanceof MouseEvent){//����¼�
			MouseEvent mouse = (MouseEvent)e;
			int type = mouse.getID();
			if(type == Event.MOUSE_DOWN){
				robot.mousePress(getMouseButton(mouse.getButton()));
			}else if(type == Event.MOUSE_UP){
				robot.mouseRelease(getMouseButton(mouse.getButton()));
			}else if(type == Event.MOUSE_MOVE){
				robot.mouseMove(mouse.getX(),mouse.getY());
			}else if(type == Event.MOUSE_DRAG){
				robot.mouseMove(mouse.getX(),mouse.getY());
			}
		}
	}
	
	//������¼�ת���ɲ����¼�
	public int getMouseButton(int button){
		if(button == MouseEvent.BUTTON1){
			return InputEvent.BUTTON1_MASK;
		}else if(button == MouseEvent.BUTTON2){
			return InputEvent.BUTTON2_MASK;
		}else if(button == MouseEvent.BUTTON3){
			return InputEvent.BUTTON3_MASK;
		}else{
			return -1;
		}
	}
}

/**
 * ������:����ͼƬ�����߳�
 * @author LLZ
 *
 */
class ImageThread extends Thread{
	//����ͼƬ,������,���ͷֱ���
	private DataOutputStream dataOut;
	public ImageThread(DataOutputStream dataOut){
		this.dataOut = dataOut;
	}
	public void run(){
		try{
			//ʵ���������˶���
			Robot robot = new Robot();
			//��Ļ�ֱ���
			Toolkit kit = Toolkit.getDefaultToolkit();
			//Dimension ��͸�
			//��ȡ��Ļ�ߴ�ֱ���
			//Dimension dm = kit.getScreenSize();
			//���η�Χ
			Rectangle rect = new Rectangle(kit.getScreenSize());
			dataOut.writeDouble(kit.getScreenSize().getHeight());
			dataOut.writeDouble(kit.getScreenSize().getWidth());
			dataOut.flush();
			BufferedImage bufImg;
			byte[] imgBytes;
			while(true){
				bufImg = robot.createScreenCapture(rect);
				imgBytes = getBytesImage(bufImg);
				//�Ƚ�ͼƬ�������С���ݹ�ȥ
				dataOut.writeInt(imgBytes.length);
				//��ͼƬ����
				dataOut.write(imgBytes);
				//�ֶ���ջ���ռ�
				dataOut.flush();
				//�ӳ�80����
				//Thread.sleep(80);
			}		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * ����:�������е�ͼƬת���ɶ���������
	 * @param bufImage
	 * @return
	 * @throws Exception
	 */
	//ѹ��ͼƬ�ķ���
	private byte[] getBytesImage(BufferedImage bufImage) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
		encoder.encode(bufImage);
		return baos.toByteArray();
	}
}
