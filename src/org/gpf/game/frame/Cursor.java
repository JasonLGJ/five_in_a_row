package org.gpf.game.frame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * ���
 * @author Liguangjin
 * @date 2017-12-18 ����4:15:22
 */
public class Cursor {

	Point point = null;
	BufferedImage cursorImage = null;
	int width,height;
	boolean show;
	
	public Cursor() {
		try {
			point = new Point();
			cursorImage = ImageIO.read(new File("res/cursor.png"));
			point.x = 15;
			point.y = 70;
			show = true;
			width = cursorImage.getWidth();
			height = cursorImage.getHeight();
			System.out.println("ʮ�ֹ����سɹ���");
		} catch (IOException e) {
			System.out.println("ʮ�ֹ�����ʧ�ܣ�");
			e.printStackTrace();
		}
	}
}