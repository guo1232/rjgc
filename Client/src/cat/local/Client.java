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
 * ������:���ƶ�
 * @author LLZ
 *
 */
public class Client extends JFrame{	
	
	//public static void main(String[] args)
	public Client()
	{
		String input = JOptionPane.showInputDialog("������Ҫ���ӵķ�����(�˿ں�)��","127.0.0.1:10000");
		//��Ҫ��ȡ��������������0��ʼ�������ֵ���Ϣ
		String host = input.substring(0,input.indexOf(":"));
		
		//��ȡ�˿ں�,��ȡ������Ķ���
		String post = input.substring(input.indexOf(":")+1);
		System.out.println(host + ":" + post);
		
		try{
			//���ӷ�����
			//(����,�˿ں�)
			//ͨ��Socket��ȡ���ݵ�������
			Socket socket = new Socket(host,Integer.parseInt(post));
			//����������
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
			ClientWindow clientWindow = new ClientWindow(objectOut);
			clientWindow.setSize((int)dataIn.readDouble(),(int)dataIn.readDouble());
			System.out.println("���ӳɹ�...");
			byte[] image;
			
			while(true){
				
				//��Server���Ӧ(int)
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
 * ������:���ƶ˴�����
 * @author LLZ
 *
 */
class ClientWindow extends JFrame{
	//������ǩ
	private JLabel background;
	//���������
	private ObjectOutputStream objectOut;
	
	//���췽��
	public ClientWindow(ObjectOutputStream objectOut){
		//���ⲿ��������ӽ���
		this.objectOut = objectOut;
		//���ñ���
		this.setTitle("QQԶ��������ϵͳ�ͻ���");
		//���óߴ�
		this.setSize(1024,768);
		//���þ���
		this.setLocationRelativeTo(null);
		//ʵ����������ǩ
		background = new JLabel();
		//����
		JPanel panel = new JPanel();
		//������ͼƬ��ӵ����
		panel.add(background);
		//�������������
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
		//����������
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
		
		//����˶�����
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
		//���������ӵ��������
		this.add(scrollPanel);
		//���ùر��˳�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�ر����ÿɼ�
		this.setVisible(true);
	}	
	public void sendEvent(InputEvent event){
		try {
			//�������������������event�а��������ͼ����¼������������Ϣ
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