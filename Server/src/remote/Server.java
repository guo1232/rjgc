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
 * 类属性:被控制端
 * @author Admin
 * @throws IOException
 */
public class Server {
	
	public static void main(String args[]) throws Exception {
		while(true){
			//建立服务器监听10000
			//ServerSocket类用于实现服务器连接，服务器套接字等待请求通过网络传入，它基于该请求执行某些操作
			//然后可能向请求者返回结果
			
			    //启动线程
				//要定义线程类ScreenThread
				//socket数据输出流
				ObjectInputStream objectIn;
			
				ServerSocket serverSocket = new ServerSocket(10000);
				System.out.println("服务器在10000端口监听...");
				System.out.println("等待连接...");
				//accpet接受
				Socket socket = serverSocket.accept();
				System.out.println("连接成功！！"+socket.getInetAddress());
				//实例化数据输出流
				DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
				//启动线程(实例化图片输出线程)
				ImageThread imageThread = new ImageThread(dataOut);
				//启动start()方法
				imageThread.start();
				objectIn = new ObjectInputStream(socket.getInputStream());
				EventThread eventThread = new EventThread(objectIn);
				eventThread.start();
			} 
			
		}
	
}

/**
 * 类描述:读取事件的发送
 */
class EventThread extends Thread{
	public ObjectInputStream objectIn;
	//模拟操作
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
				//处理事件
				actionEvent(event);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void actionEvent(InputEvent e){
		//键盘事件
		if(e instanceof KeyEvent){
			KeyEvent key = (KeyEvent)e;
			//获取到键盘的事件编号
			int type = key.getID();
			if(type == Event.KEY_PRESS){
				robot.keyPress(key.getKeyCode());
			}else if(type == Event.KEY_RELEASE){
				robot.keyRelease(key.getKeyCode());
			}
		}else if(e instanceof MouseEvent){//鼠标事件
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
	
	//把鼠标事件转换成操作事件
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
 * 类描述:本地图片传输线程
 * @author LLZ
 *
 */
class ImageThread extends Thread{
	//发送图片,二进制,发送分辨率
	private DataOutputStream dataOut;
	public ImageThread(DataOutputStream dataOut){
		this.dataOut = dataOut;
	}
	public void run(){
		try{
			//实例化机器人对象
			Robot robot = new Robot();
			//屏幕分辨率
			Toolkit kit = Toolkit.getDefaultToolkit();
			//Dimension 宽和高
			//获取屏幕尺寸分辨率
			//Dimension dm = kit.getScreenSize();
			//矩形范围
			Rectangle rect = new Rectangle(kit.getScreenSize());
			dataOut.writeDouble(kit.getScreenSize().getHeight());
			dataOut.writeDouble(kit.getScreenSize().getWidth());
			dataOut.flush();
			BufferedImage bufImg;
			byte[] imgBytes;
			while(true){
				bufImg = robot.createScreenCapture(rect);
				imgBytes = getBytesImage(bufImg);
				//先将图片的数组大小传递过去
				dataOut.writeInt(imgBytes.length);
				//将图片传递
				dataOut.write(imgBytes);
				//手动清空缓存空间
				dataOut.flush();
				//延迟80毫秒
				//Thread.sleep(80);
			}		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 方法:将缓存中的图片转换成二进制数组
	 * @param bufImage
	 * @return
	 * @throws Exception
	 */
	//压缩图片的方法
	private byte[] getBytesImage(BufferedImage bufImage) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(baos);
		encoder.encode(bufImage);
		return baos.toByteArray();
	}
}
