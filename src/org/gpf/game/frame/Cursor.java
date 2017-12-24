package org.gpf.game.frame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 光标
 * @author Liguangjin
 * @date 2017-12-18 下午4:15:22
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
			System.out.println("十字光标加载成功！");
		} catch (IOException e) {
			System.out.println("十字光标加载失败！");
			e.printStackTrace();
		}
	}
}