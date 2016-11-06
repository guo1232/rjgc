package cat.Main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import cat.ScreenShot.ScreenShot;
import cat.client.CatChatroom;
import cat.function.CatBean;
import cat.function.ClientBean;
import cat.local.*;
import cat.login.CatLogin;
import cat.login.CatResign;
import cat.util.CatUtil;

public class MainInterface extends JFrame {
	
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	public static HashMap<String, ClientBean> onlines;

	/**
	 * ����Ӧ�ó���(Java�������)
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					// ������½����
					MainInterface frame = new MainInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * �������JFrame
	 */
	public MainInterface() {
		setTitle("Զ�������ϵͳ������\n");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(250, 120, 850, 500);
		setResizable(false);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
						"images/13.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("images/14.jpg"));
		btnNewButton.setBounds(105, 310, 90, 45);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);

		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("images/17.jpg"));
		btnNewButton_1.setBounds(380, 315, 90, 45);	
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton_1);

		final JButton btnNewButton_2 = new JButton();
		btnNewButton_2.setIcon(new ImageIcon("images/16.jpg"));
		btnNewButton_2.setBounds(650, 310, 90, 45);
		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton_2);
		
				
		//Զ��Э�����水ť����
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(true);
						
				Client frame = new Client();
				//frame = new Client();
				// ��ʾע�����
				frame.setVisible(true);
				// ���ص���½����
				setVisible(true);
			}
		});
		
		
		//������水ť����
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_1.setEnabled(true);
				CatLogin frame = new CatLogin();
				// ��ʾע�����
				frame.setVisible(true);
				// ���ص���½����
				setVisible(true);
			}
		});
		
		
		//��ͼ���水ť����
		btnNewButton_2.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				btnNewButton_2.setEnabled(true);
				ScreenShot frame = new ScreenShot();
				setVisible(true);
				System.out.println("�����ͼ����������...");
			}
		});
	}
}
