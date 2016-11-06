package com.qq.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * 类名称:RobotUtil
 * @author LLZ
 * @version 1.0
 */
public class RobotUtil {

	public static void main(String[] args){
		try {
			//创建一个Robot实例,变量名nnnec
			Robot nnnec = new Robot();
			//移动鼠标到指定的坐标
			nnnec.mouseMove(982, 297);
			//睡眠一定时间(1毫秒)
			nnnec.delay(1000);
			nnnec.mouseMove(1499, 463);
			//鼠标点击事件 鼠标按下 
			nnnec.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			//鼠标放开
			nnnec.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			//键盘输入操作 键盘按下
			nnnec.keyPress('A');
			//键盘放开
			nnnec.keyRelease('A');
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
