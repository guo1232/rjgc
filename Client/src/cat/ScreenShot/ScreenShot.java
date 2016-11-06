package cat.ScreenShot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

/**
 * 
 * java��������QQ��ͼ���
 * @author LLZ
 * @version 2.0
 *
 *1.��Ҫ�д��崰��
 *2.��Ҫ��ȡһ����Ļ��ͼ
 *3.��Ҫ�ṩ���û�ѡ��һ������Ĺ���
 *4.����ͼƬ(�趨·��)
 *
 */
public class ScreenShot extends JFrame{

	/**
	 * java������ں���
	 * @param args
	 */
	public ScreenShot(){
		
		//��һ��ע��ȫ�ֿ�ݼ�
		//����ALT + SΪ��ݼ�
		JIntellitype.getInstance().registerHotKey(1,
				JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT,(int)'S');
		
		//��Ӽ�����
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			
			@Override
			public void onHotKey(int code){
				//����������Ϊ1���Ϳ�ʼ����
				if(code == 1){
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				}
			}
		});
	}
}


/**
 * ��ȡ��Ļ��������
 * @author LLZ
 * JFrame ����
 */

//����һ����
class MainFrame extends JFrame{
	
	//��ͼ��壬���ÿ�
	private ShotImagePanel imagePanel = null;	
	//������壬���ÿ�
	private ActionPanel actionPanel = null;
	
	//���췽��,����ʵ����һ����
	public MainFrame(){
		
		//�������û�б߿�Ҫ���ô���ȫ��
		
		//���ô������
		//setTitle("Java����QQ�����ͼ���");		
		
		//borderLayout���ַ�ʽ
		//ȥ��border�߿�
		setLayout(null);			
		//ȥ������ı߿�
		setUndecorated(true);
		
		//��ʼ����������
		initComp();
		//�ô���ȫ����ʾ
		setFullScreen();
		
		//ȫ����ʾҪ�ر�
		//���һ�����̵ļ����¼�
		this.addKeyListener(new KeyAdapter(){
			
			@Override
			public void keyPressed(KeyEvent e){
				//���һ�����̼���
				//����㰴���˼��̵�ĳ��������ִ�ж�Ӧ����
				
				//�ж��û����µļ�ESC,�˳�����
				if(e.getKeyChar() == KeyEvent.VK_ESCAPE){
					
					//�˳���������
					setVisible(false);
				}			
			}
		});		
	}	
	
	/**
	 * ���ô���ȫ����ʾ
	 */	
	private void setFullScreen(){
		
		//��Ҫ��ȡ�û�ͼ����ʾ�豸�Ķ���
		GraphicsDevice device = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		
		//���֧��ȫ��(�ж��Ƿ�ȫ��)
		if(device.isFullScreenSupported()){
			device.setFullScreenWindow(this);
		}	
	}
	
	/**
	 * ��ʼ�����ڵ�����
	 */
	private void initComp(){
		
		
		//�½�һ���������
		actionPanel = new ActionPanel(this);
		this.add(actionPanel);
		//�����(0,0)
		actionPanel.setLocation(0,0);
		
		//�½�һ����ͼ���
		imagePanel = new ShotImagePanel();
		//�ѽ�ͼ�����ӵ����ڣ�this����Mainframeʵ�����Ķ���
		//this����add()����
		this.add(imagePanel);
		
	}
	
	/**
	 * 
	 * ��ʾ������
	 */
	public void showTool(int x,int y){
		
		this.actionPanel.setLocation(x,y);
		this.actionPanel.setVisible(true);
		this.actionPanel.repaint();
	}
	
	/**
	 * ���ع�����
	 */
	public void hideTool(){
		
		//����Ϊ���ɼ�
		this.actionPanel.setVisible(false);
	}
	
	/**
	 * ����ͼƬ
	 */
	public void saveImage(){
		
		//�˳�ȫ����״̬
		//����Ҫ��ȡ��ʾ����״̬
		GraphicsDevice device = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		
		//�������ȫ��״̬��Ҫ�˳�ȫ��
		if(device.isFullScreenSupported()){
			device.setFullScreenWindow(null);
		}
		
		this.imagePanel.saveImage();
	}
	

}

/**
 * ����չʾһ��ģ̬����(����δ��ȡ������ɫ��䵭)
 * ��ͼ���
 *
 */
class ShotImagePanel extends JPanel{
	
	//��ʽ��ʱ��,��ʽ��Ϊ"yyyymmmmddHH"���͵��ַ���
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyymmmmddHHmmss");
		
	//�����������������,�����û����������ʼ������յ�����
	private int startx,starty,endx,endy;
	
	//��Ҫ��ȡ��Ļ�ĳߴ�
	private BufferedImage image = null;
	//�Խ�ȡ��ͼƬ���д���������ɫ
	private BufferedImage tempImage = null;
	//����Ҫ�����ͼƬ��������
	private BufferedImage saveImage = null;
	
	//����MainFrame˽�ж���
	private MainFrame parent = null;
	
	/**
	 * ���췽��
	 */	
	public ShotImagePanel(){
		
		this.parent = parent;
		//��Ҫ��ȡ��Ļ�ߴ�
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		//�趨����İ��¼�,��ȡ��Ļ���ĸ�����
		this.setBounds(0,0,d.width,d.height);
		
		
		//��ȡ��Ļ
		try{
			//����robot�����ȡ��Ļ��ͼ
			Robot robot = new Robot();
			image = robot.createScreenCapture(new Rectangle(0,0,d.width,d.height));			
		}catch(Exception e){
			
			System.out.println("��Ǹ����ʼ��ʧ��");	
		}
		
		//������ļ��� click(���) realse(�ͷ�) press(˫��)
		//MouseAdapter��һ��������,������д����
		this.addMouseListener(new MouseAdapter(){
			
			//��д�����෽��mousePressed()
			//�û��������ʱ����Ҫ���Ȼ�ȡ�����λ��
			@Override
			public void mousePressed(MouseEvent e){
				
				//��ȡ����x��y����ֵ			
				startx = e.getX();
				starty = e.getY();
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				
				ShotImagePanel.this.parent.showTool(e.getX(),e.getY());
			}
			
		});
		
		//���������MouseListenerdrag move
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				
				// ���������û���ɿ�����ʵʱ�������x,y����
				endx = e.getX();
				endy = e.getY();
				
				//����һ����ʱ��ͼ�񣬲��ϵػ���һ��ͼƬ
				Image tempImage = createImage(ShotImagePanel.this.getWidth(),ShotImagePanel.this.getHeight());
				
				//��ȡһ������(Ҫ��ͼ�����Ҫ�л���)
				Graphics g = tempImage.getGraphics();
				//ShotImagePanel.this.tempImage�Ǵ�������ͼƬ
				g.drawImage(ShotImagePanel.this.tempImage,0,0,null);
				
				//��ȡ�������е�С���Ǹ�
				int x = Math.min(startx,endx);
				int y = Math.min(starty,endy);
				
				//��ÿ�Ⱥ͸߶�
				//Math()��������(�����յ�)֮��Ĳ�͵õ���ȡ��Ļ�ĳ��Ϳ�
				int width = Math.abs(startx - endx) +1;
				int height = Math.abs(starty - endy) + 1;
				
				//��������,���û�����ɫ
				g.setColor(Color.pink);
				//��ʼ������x,y,��ȡ������width��height��͸�
				g.drawRect(x - 1, y - 1, width + 1, height + 1);
				
				
				/**
				 * ��ȡС����ͼƬ
				 */	
				//��������ȡ��һС��ͼƬ����
				//(x,y)��ʼ��ȡ������(width,height)����ȡ�Ŀ�Ⱥ͸߶�
				saveImage = image.getSubimage(x, y, width, height);
				//��ȡ������ͼƬ�Ժ�Ҫ����ͼƬ
				g.drawImage(saveImage,x,y,null);
				
				//��0,0��ʼ���ƣ���Ӧֵ��ShotImagePanel.this
				ShotImagePanel.this.getGraphics().drawImage(tempImage,0,0,ShotImagePanel.this);
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO �Զ����ɵķ������
				
			}		
		});
	}
	

	@Override
	public void paint(Graphics g){
		
		//super���ø���JPanel��paint()����,��������ø��࣬�ܶ�����Ķ���������Ƴ���
		super.paint(g);
		//��ȡ��ͼƬҪ������BufferImage����
		
		//����ͼƬ
		RescaleOp ro = new RescaleOp(0.6f,0,null);
		//��ԭʼͼƬ���д���
		tempImage = ro.filter(image, null);
		//��ͼƬ���Ƴ���,���ƾ��������Ժ�tempImage��ͼƬ,��(0,0)��ʼ����
		g.drawImage(tempImage,0,0,this);
	}
	
	/**
	 * ����ͼƬ����
	 */
	public void saveImage(){
		
		//�����ñ���ͼƬ��·��
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("�����ͼ");
		
		//�ļ�������(����ֻ����ͼƬ�ļ�,��׺��������)
		FileNameExtensionFilter filer = new FileNameExtensionFilter("JPG","jpg");
		jfc.setFileFilter(filer);
		
		//��ʽ������
		String fileName = sdf.format(new Date());
		
		//�õ��û���Ŀ¼·��
		File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		
		//File.separator�ļ��ķָ���
		//+ ".jpg"Ϊ��ȡͼƬ�Ӻ�׺��(�û������Լ���ƣ��˲���ʡ��)
		//File defaultFile = new File(filePath + File.separator + fileName + ".jpg");
		File defaultFile = new File(filePath + File.separator + fileName);
		jfc.setSelectedFile(defaultFile);
		
		//���屣��ͼƬ����ʾ״̬
		int flag = jfc.showSaveDialog(this);
		//�ж��û����µ��Ǳ�����ť����ȡ����ť
		if(flag == JFileChooser.APPROVE_OPTION){
			
			//��ȡ�û�ѡ��ı����ȡͼƬ��·��
			File file = jfc.getSelectedFile();
			String path = file.getPath();
			
			//����ļ���·��������"jpg""JPG"��β,��ͳһ����Ϊ��"jpg"��β
			if(!path.endsWith("jpg") || !path.endsWith("JPG")){
				
				path += ".jpg";
			}
			try{
				
				ImageIO.write(saveImage, "jpg", new File(path));
			}catch(Exception e){
				
				//�����쳣
				System.out.println("�����ͼ�쳣");
			}
		}	
		//�˳�����
		//System.exit(0);
		parent.setVisible(false);
	}
}

/**
 * �������(�������)
 * �൱�ڹ�����
 *
 */
class ActionPanel extends JPanel{
	
	private MainFrame parent = null;
	
	//���췽����Ĭ�ϵ���
	public ActionPanel(MainFrame parent){
		
		this.parent = parent;
		setLayout(new BorderLayout());
		//���ò������Ĵ�С
		setSize(80,40);
		//���ò������ɼ�
		setVisible(true);
		
		initComp();
		
	}
	
	/**
	 * ��ʼ�������������ӵ������
	 */
	private void initComp(){
		
		//������
		JToolBar actionBar = new JToolBar("");
		//�ù��������ܹ���
		actionBar.setFloatable(false);
		
		//���ð�ť,���ڱ�����Ƭ
		//��ťΪһ��ͼƬ
		JButton saveBtn = new JButton(new ImageIcon("images/save.gif"));
		saveBtn.addActionListener(new ActionListener(){
			
			//���������¼�
			
			@Override
			public void actionPerformed(ActionEvent e){
				
				//�����ͼ
				ActionPanel.this.parent.saveImage();
				
			}
		});	
		actionBar.add(saveBtn);
		

		//���水ť
		JButton closeBtn = new JButton(new ImageIcon("images/close.gif"));
		closeBtn.addActionListener(new ActionListener(){
			
			//���������¼�
			@Override
			public void actionPerformed(ActionEvent e){
				
				//System.exit(0);
				ActionPanel.this.parent.setVisible(false);
			}
		});
		actionBar.add(closeBtn);
		
		this.add(actionBar,BorderLayout.NORTH);
	}
}

