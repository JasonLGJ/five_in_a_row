package org.gpf.game.test;

import javax.sound.sampled.LineUnavailableException;

import org.gpf.game.frame.FiveChessFrame;

public class Test {
	
	private static final int WIDTH = 650; //��Ϸ����Ŀ��
	private static final int HEIGHT = 600;

	public static void main(String[] args){
		
		/* ��ȡ���������п�������
			String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			for (String f : fonts) {
				System.out.println(f);
			}
		*/
		try {
			new FiveChessFrame(WIDTH, HEIGHT); // ������Ϸ
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

}