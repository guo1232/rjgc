package com.qq.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * ������:RobotUtil
 * @author LLZ
 * @version 1.0
 */
public class RobotUtil {

	public static void main(String[] args){
		try {
			//����һ��Robotʵ��,������nnnec
			Robot nnnec = new Robot();
			//�ƶ���굽ָ��������
			nnnec.mouseMove(982, 297);
			//˯��һ��ʱ��(1����)
			nnnec.delay(1000);
			nnnec.mouseMove(1499, 463);
			//������¼� ��갴�� 
			nnnec.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			//���ſ�
			nnnec.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			//����������� ���̰���
			nnnec.keyPress('A');
			//���̷ſ�
			nnnec.keyRelease('A');
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
