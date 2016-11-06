package cat.local;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 类描述:控制端
 * @author LLZ
 *
 */
public class Client extends JFrame{	
	
	//public static void main(String[] args)
	public Client()
	{
		String input = JOptionPane.showInputDialog("请输入要连接的服务器(端口号)：","127.0.0.1:10000");
		//先要获取服务器主机，从0开始到：部分的信息
		String host = input.substring(0,input.indexOf(":"));
		
		//获取端口号,获取：后面的东西
		String post = input.substring(input.indexOf(":")+1);
		System.out.println(host + ":" + post);
		
		try{
			//连接服务器
			//(主机,端口号)
			//通过Socket获取数据的输入流
			Socket socket = new Socket(host,Integer.parseInt(post));
			//数据输入流
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
			ClientWindow clientWindow = new ClientWindow(objectOut);
			clientWindow.setSize((int)dataIn.readDouble(),(int)dataIn.readDouble());
			System.out.println("连接成功...");
			byte[] image;
			
			while(true){
				
				//和Server相对应(int)
				image = new byte[dataIn.readInt()];
				dataIn.readFully(image);
				
				clientWindow.repaintImage(image);	
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
}

/**
 * 类描述:控制端窗体类
 * @author LLZ
 *
 */
class ClientWindow extends JFrame{
	//背景标签
	private JLabel background;
	//对象输出流
	private ObjectOutputStream objectOut;
	
	//构造方法
	public ClientWindow(ObjectOutputStream objectOut){
		//将外部的输出流加进来
		this.objectOut = objectOut;
		//设置标题
		this.setTitle("QQ远程桌面监控系统客户端");
		//设置尺寸
		this.setSize(1024,768);
		//设置居中
		this.setLocationRelativeTo(null);
		//实例化背景标签
		background = new JLabel();
		//容器
		JPanel panel = new JPanel();
		//将背景图片添加到面板
		panel.add(background);
		//创建滚动条面板
		JScrollPane scrollPanel = new JScrollPane(panel);
		addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				sendEvent(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				sendEvent(e);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				sendEvent(e);
			}
			
		});
		//鼠标操作监听
		background.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				sendEvent(e);
			}
			
		});
		
		//鼠标运动监听
		background.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				sendEvent(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				sendEvent(e);
			}
			
		});
		//将滚动条加到面板里面
		this.add(scrollPanel);
		//设置关闭退出进程
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//关闭设置可见
		this.setVisible(true);
	}	
	public void sendEvent(InputEvent event){
		try {
			//将对象输出到服务器端event中包含了鼠标和键盘事件操作的相关信息
			objectOut.writeObject(event);
			objectOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void repaintImage(byte[] image){
		background.setIcon(new ImageIcon(image));
		this.repaint();
	}
}