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
 * java开发桌面QQ截图软件
 * @author LLZ
 * @version 2.0
 *
 *1.需要有窗体窗口
 *2.需要获取一个屏幕截图
 *3.需要提供给用户选择一个区域的功能
 *4.保存图片(设定路径)
 *
 */
public class ScreenShot extends JFrame{

	/**
	 * java程序入口函数
	 * @param args
	 */
	public ScreenShot(){
		
		//第一步注册全局快捷键
		//设置ALT + S为快捷键
		JIntellitype.getInstance().registerHotKey(1,
				JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT,(int)'S');
		
		//添加监听器
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			
			@Override
			public void onHotKey(int code){
				//如果输入程序为1，就开始运行
				if(code == 1){
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				}
			}
		});
	}
}


/**
 * 截取屏幕的主窗口
 * @author LLZ
 * JFrame 窗口
 */

//创建一个类
class MainFrame extends JFrame{
	
	//截图面板，先置空
	private ShotImagePanel imagePanel = null;	
	//工具面板，先置空
	private ActionPanel actionPanel = null;
	
	//构造方法,用于实例化一个类
	public MainFrame(){
		
		//这个窗口没有边框，要设置窗口全屏
		
		//设置窗体标题
		//setTitle("Java开发QQ桌面截图软件");		
		
		//borderLayout布局方式
		//去除border边框
		setLayout(null);			
		//去除外面的边框
		setUndecorated(true);
		
		//初始化窗口内容
		initComp();
		//让窗体全屏显示
		setFullScreen();
		
		//全屏显示要关闭
		//添加一个键盘的监听事件
		this.addKeyListener(new KeyAdapter(){
			
			@Override
			public void keyPressed(KeyEvent e){
				//添加一个键盘监听
				//如果你按下了键盘的某个键，就执行对应操作
				
				//判断用户按下的键ESC,退出程序
				if(e.getKeyChar() == KeyEvent.VK_ESCAPE){
					
					//退出程序运行
					setVisible(false);
				}			
			}
		});		
	}	
	
	/**
	 * 设置窗口全屏显示
	 */	
	private void setFullScreen(){
		
		//先要获取用户图像显示设备的对象
		GraphicsDevice device = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		
		//如果支持全屏(判断是否全屏)
		if(device.isFullScreenSupported()){
			device.setFullScreenWindow(this);
		}	
	}
	
	/**
	 * 初始化窗口的内容
	 */
	private void initComp(){
		
		
		//新建一个工具面板
		actionPanel = new ActionPanel(this);
		this.add(actionPanel);
		//坐标点(0,0)
		actionPanel.setLocation(0,0);
		
		//新建一个截图面板
		imagePanel = new ShotImagePanel();
		//把截图面板添加到窗口，this就是Mainframe实例化的对象
		//this调用add()方法
		this.add(imagePanel);
		
	}
	
	/**
	 * 
	 * 显示工具栏
	 */
	public void showTool(int x,int y){
		
		this.actionPanel.setLocation(x,y);
		this.actionPanel.setVisible(true);
		this.actionPanel.repaint();
	}
	
	/**
	 * 隐藏工具栏
	 */
	public void hideTool(){
		
		//设置为不可见
		this.actionPanel.setVisible(false);
	}
	
	/**
	 * 保存图片
	 */
	public void saveImage(){
		
		//退出全屏的状态
		//首先要获取显示器的状态
		GraphicsDevice device = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		
		//如果还是全屏状态就要退出全屏
		if(device.isFullScreenSupported()){
			device.setFullScreenWindow(null);
		}
		
		this.imagePanel.saveImage();
	}
	

}

/**
 * 用于展示一个模态窗体(就是未截取部分颜色会变淡)
 * 截图面板
 *
 */
class ShotImagePanel extends JPanel{
	
	//格式化时间,格式化为"yyyymmmmddHH"类型的字符串
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyymmmmddHHmmss");
		
	//创建对象来保存参数,定义用户点击鼠标的起始坐标和终点坐标
	private int startx,starty,endx,endy;
	
	//需要获取屏幕的尺寸
	private BufferedImage image = null;
	//对截取的图片进行处理，加深颜色
	private BufferedImage tempImage = null;
	//将需要保存的图片保存起来
	private BufferedImage saveImage = null;
	
	//构造MainFrame私有对象
	private MainFrame parent = null;
	
	/**
	 * 构造方法
	 */	
	public ShotImagePanel(){
		
		this.parent = parent;
		//需要获取屏幕尺寸
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		//设定窗体的绑定事件,获取屏幕的四个参数
		this.setBounds(0,0,d.width,d.height);
		
		
		//截取屏幕
		try{
			//创建robot对象截取屏幕截图
			Robot robot = new Robot();
			image = robot.createScreenCapture(new Rectangle(0,0,d.width,d.height));			
		}catch(Exception e){
			
			System.out.println("抱歉！初始化失败");	
		}
		
		//添加鼠标的监听 click(点击) realse(释放) press(双击)
		//MouseAdapter是一个抽象类,可以重写方法
		this.addMouseListener(new MouseAdapter(){
			
			//重写抽象类方法mousePressed()
			//用户点击鼠标的时候需要首先获取点击的位置
			@Override
			public void mousePressed(MouseEvent e){
				
				//获取起点的x和y坐标值			
				startx = e.getX();
				starty = e.getY();
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				
				ShotImagePanel.this.parent.showTool(e.getX(),e.getY());
			}
			
		});
		
		//添加鼠标监听MouseListenerdrag move
		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				
				// 不管鼠标有没有松开，都实时获得鼠标的x,y坐标
				endx = e.getX();
				endy = e.getY();
				
				//创建一个临时的图像，不断地绘制一个图片
				Image tempImage = createImage(ShotImagePanel.this.getWidth(),ShotImagePanel.this.getHeight());
				
				//获取一个画笔(要画图像必须要有画笔)
				Graphics g = tempImage.getGraphics();
				//ShotImagePanel.this.tempImage是处理过后的图片
				g.drawImage(ShotImagePanel.this.tempImage,0,0,null);
				
				//获取两个数中的小的那个
				int x = Math.min(startx,endx);
				int y = Math.min(starty,endy);
				
				//获得宽度和高度
				//Math()计算他们(起点和终点)之间的差，就得到截取屏幕的长和宽
				int width = Math.abs(startx - endx) +1;
				int height = Math.abs(starty - endy) + 1;
				
				//绘制区域,设置画笔颜色
				g.setColor(Color.pink);
				//起始坐标是x,y,截取的区域width和height宽和高
				g.drawRect(x - 1, y - 1, width + 1, height + 1);
				
				
				/**
				 * 截取小区域图片
				 */	
				//保存所截取的一小块图片区域
				//(x,y)开始截取的坐标(width,height)所截取的宽度和高度
				saveImage = image.getSubimage(x, y, width, height);
				//截取并保存图片以后还要绘制图片
				g.drawImage(saveImage,x,y,null);
				
				//从0,0开始绘制，响应值是ShotImagePanel.this
				ShotImagePanel.this.getGraphics().drawImage(tempImage,0,0,ShotImagePanel.this);
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}		
		});
	}
	

	@Override
	public void paint(Graphics g){
		
		//super调用父类JPanel的paint()方法,如果不调用父类，很多基本的东西不会绘制出来
		super.paint(g);
		//截取的图片要保存在BufferImage里面
		
		//处理图片
		RescaleOp ro = new RescaleOp(0.6f,0,null);
		//对原始图片进行处理
		tempImage = ro.filter(image, null);
		//把图片绘制出来,绘制经过处理以后tempImage的图片,从(0,0)开始绘制
		g.drawImage(tempImage,0,0,this);
	}
	
	/**
	 * 保存图片方法
	 */
	public void saveImage(){
		
		//先设置保存图片的路径
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("保存截图");
		
		//文件过滤器(比如只过滤图片文件,后缀名过滤器)
		FileNameExtensionFilter filer = new FileNameExtensionFilter("JPG","jpg");
		jfc.setFileFilter(filer);
		
		//格式化日期
		String fileName = sdf.format(new Date());
		
		//得到用户的目录路径
		File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		
		//File.separator文件的分隔符
		//+ ".jpg"为截取图片加后缀名(用户可以自己设计，此步可省略)
		//File defaultFile = new File(filePath + File.separator + fileName + ".jpg");
		File defaultFile = new File(filePath + File.separator + fileName);
		jfc.setSelectedFile(defaultFile);
		
		//定义保存图片的显示状态
		int flag = jfc.showSaveDialog(this);
		//判断用户按下的是保留按钮还是取消按钮
		if(flag == JFileChooser.APPROVE_OPTION){
			
			//获取用户选择的保存截取图片的路径
			File file = jfc.getSelectedFile();
			String path = file.getPath();
			
			//如果文件的路径不是以"jpg""JPG"结尾,则统一设置为以"jpg"结尾
			if(!path.endsWith("jpg") || !path.endsWith("JPG")){
				
				path += ".jpg";
			}
			try{
				
				ImageIO.write(saveImage, "jpg", new File(path));
			}catch(Exception e){
				
				//捕获异常
				System.out.println("保存截图异常");
			}
		}	
		//退出程序
		//System.exit(0);
		parent.setVisible(false);
	}
}

/**
 * 工具面板(操作面板)
 * 相当于工具条
 *
 */
class ActionPanel extends JPanel{
	
	private MainFrame parent = null;
	
	//构造方法，默认调用
	public ActionPanel(MainFrame parent){
		
		this.parent = parent;
		setLayout(new BorderLayout());
		//设置操作面板的大小
		setSize(80,40);
		//设置操作面板可见
		setVisible(true);
		
		initComp();
		
	}
	
	/**
	 * 初始化组件，并且添加到面板中
	 */
	private void initComp(){
		
		//工具条
		JToolBar actionBar = new JToolBar("");
		//让工具条不能够动
		actionBar.setFloatable(false);
		
		//设置按钮,用于保存照片
		//按钮为一张图片
		JButton saveBtn = new JButton(new ImageIcon("images/save.gif"));
		saveBtn.addActionListener(new ActionListener(){
			
			//添加鼠标点击事件
			
			@Override
			public void actionPerformed(ActionEvent e){
				
				//保存截图
				ActionPanel.this.parent.saveImage();
				
			}
		});	
		actionBar.add(saveBtn);
		

		//保存按钮
		JButton closeBtn = new JButton(new ImageIcon("images/close.gif"));
		closeBtn.addActionListener(new ActionListener(){
			
			//添加鼠标点击事件
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

