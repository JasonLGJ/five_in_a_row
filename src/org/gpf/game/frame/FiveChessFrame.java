package org.gpf.game.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FiveChessFrame extends JFrame implements MouseListener,Runnable,MouseMotionListener {

	private static final long serialVersionUID = 1L;
	int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width; // �����Ļ�ߴ�
	int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

	int x = 0, y = 0; // ��������
	
	int[][] allChess = new int[19][19]; // ����֮ǰ�¹������ӵ�״̬��0��ʾû�����ӣ�1���ӣ�2����
	
	String message = "�ڷ�����";
	
	boolean isBlack = true; // �жϵ�ǰ�Ǻ��廹�ǰ��������һ��
	
	boolean canPlay = true; // �ж��Ƿ������������

	BufferedImage bgImage = null; // ����ͼƬ
	
	int maxTime = 0; // �������ӵ�е�ʱ�䣨�룩
	
	Thread thread = new Thread(this); // ����ʱ���߳���
	
	int balckTime = 0,whiteTime = 0; // �ڷ��Ͱ׷���ʣ��ʱ��
	
	String balckMessage = "00:00:00",whiteMessage = "00:00:00"; // ����˫��ʣ��ʱ�����ʾ��Ϣ,Ĭ����0��ʾ������
	
	boolean musicOn = true; // �Ƿ�����������
	
	Thread bgMusicThread = new Thread(new BgMusic()); // ���������߳�
	
	Cursor cursor = null; // ʮ�ֹ��
	
	@SuppressWarnings("deprecation")
	public FiveChessFrame(int width, int height) throws LineUnavailableException {
		this.setTitle("��������Ϸ");
		this.setSize(width, height);
		this.setLocation((screenWidth - width) / 2, (screenHeight - height) / 2); // ��Ϸ���������ʾ
		this.setResizable(false); // �����С���ɱ�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addMouseListener(this); // ע���������¼�
		this.addMouseMotionListener(this);
		this.setVisible(true);
		
		cursor = new Cursor(); 
		thread.start();
		thread.suspend();
	 //����ʱ�̹߳���
		if (musicOn) {
			bgMusicThread.start();
		}
		this.repaint(); // ˢ����Ļ�����������Ϸʱ�ĺ�������
	}

	/**
	 * ��Ⱦ����Ԫ��
	 */
	@Override
	public void paint(Graphics g) {
		/* ���ر���ͼƬ */
		try {
			bgImage = ImageIO.read(new File("res/bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.paint(g);;
		Graphics2D g2 = (Graphics2D)g;
		boolean flag = g2.drawImage(bgImage, 0, 0, this);
		if (flag) {
			System.out.println("ͼƬ���سɹ���");
		}

		/* ������Ϣ������ */
		g2.setFont(new Font("����", Font.BOLD, 20));
		g2.setColor(Color.YELLOW);
		g2.drawString("��Ϸ��Ϣ��" + message, 150, 60);

		g2.setFont(new Font("����", Font.PLAIN, 18));
		g2.setColor(Color.GREEN);
		g2.drawString("�ڷ�ʱ�䣺" + balckMessage, 30, 550);
		g2.drawString("�׷�ʱ�䣺" + whiteMessage, 300, 550);
		
		g2.setFont(new Font("����", Font.BOLD, 18));
		g2.setColor(Color.CYAN);
		g2.drawRect(530, 100, 100, 30);
		g2.drawString("����Ϸ", 552, 123);
		g2.drawRect(530, 170, 100, 30);
		g2.drawString("��Ϸ����", 545, 193);
		g2.drawRect(530, 240, 100, 30);
		g2.drawString("��Ϸ˵��", 545, 263);
		g2.drawRect(530, 300, 100, 30);
		g2.drawString("����" + (musicOn?"��":"��"), 545, 320);
		g2.drawRect(530, 350, 100, 30);
		g2.drawString("����", 555, 373);
		g2.drawRect(530, 420, 100, 30);
		g2.drawString("����", 555, 443);
		g2.drawRect(530, 490, 100, 30);
		g2.drawString("�˳�", 555, 513);
		
		g2.setColor(Color.RED);
		g2.setFont(new Font("����", Font.BOLD, 28));
		g2.drawString("������Ʒ���д����ơ�", 320, 590);

		g2.setColor(Color.ORANGE);
		/* ��ʼ�����̸�(18 * 18) */
		for (int i = 0; i < 19; i++) {
			g2.drawLine(15, 70 + 25 * i, 465, 70 + 25 * i); // ���ƺ���
			g2.drawLine(15 + 25 * i, 70, 15 + 25 * i, 520);// ��������
		}

		if (cursor.show) {
			g2.drawImage(cursor.cursorImage, cursor.point.x - cursor.width/2, cursor.point.y - cursor.width/2,null);
		}
		
		/* ��ע9�������λ  */
		g2.setColor(Color.GREEN);
		for (int i = 0; i < 3; i++) {
			for(int j = 0;j < 3;j++){
				g2.fillOval(7 + 3 * 25 + 150 * i, 63 + 3 * 25 + 150 * j, 15, 15);
				//��������Ϊֱ���ô������ģ�û�����������Ի��Ƶ���Щƫ�����Ϊ��7��63���������ǡ�15��70����
			}
		}
		
		/* ����ȫ������ */
		g2.setColor(Color.BLACK);
		for(int i = 0;i < 19;i++){
			for(int j = 0;j < 19;j++){
				
				int tempX = 25 * i + 15; // �õ����ӵ�׼ȷ����
				int tempY = 25 * j + 70;
				
				cursor.point.x = tempX;
				cursor.point.y = tempY;
				
				if (allChess[i][j] == 1) {
					// ���ƺ���
					g2.fillOval(tempX - 11, tempY - 11, 22, 22);
				}
				if (allChess[i][j] == 2) {
					// ���ư���
					g2.setColor(Color.WHITE);
					g2.fillOval(tempX - 11, tempY - 11, 22, 22);//ʵ��
					g2.setColor(Color.BLACK);
					g2.drawOval(tempX - 11, tempY - 11, 22, 22);//����
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		cursor.show = false;
		x = e.getX(); // �õ����ĵ��λ��
		y = e.getY();
		System.out.println("������꣺(" + x + "," + y + ")");
		
		if (canPlay) {
			/* �жϵ���������Ƿ���18*18�ķ�Χ֮�� */
			if (x >= 15 && x <= 465 && y >= 70 && y <= 520) {
//				x = (x - 15) / 25; // �õ������ʮ�ֵ�,�����ȵõ�0~18��������Ȼ��ͨ�������������ÿ�еļ�����
//				y = (y - 70) / 25;
				
				/* ���ϵ��ж����������x������ڵ�Ԫ������ߣ�����Ӧ�������Ҳ��ʮ���ߣ�y����ͬ�� */
				if (((x - 15) % 25) >= 13) {
					x = ((x - 15) / 25) + 1;
				} else
					x = (x - 15) / 25;
				if (((y - 70) % 25) >= 13) {
					y = ((y - 70) / 25) + 1;
				} else
					y = (y - 70) / 25;
				
				System.out.println("x = " + x + ", y = " + y);
				System.out.println("��ʱallChess[x][y] = " + allChess[x][y]);
				
				if (allChess[x][y] == 0) {
					/* �жϵ�ǰҪ�µ���ʲô���ӣ� */
					if (isBlack) {
						allChess[x][y] = 1;
						isBlack = false;
						message = "�ֵ��׷�";
					} else {
						allChess[x][y] = 2;
						isBlack = true;
						message = "�ֵ��ڷ�";
					}

					/* �жϵ�ǰ�����Ƿ����������5���� */
					boolean winFlag = this.checkWin();
					if (winFlag) {
						JOptionPane.showMessageDialog(this, "��Ϸ����," + (allChess[x][y] == 1 ? "�ڷ�Ӯ��" : "�׷�Ӯ��"));
						canPlay = false;
					}
				} else {
					JOptionPane.showMessageDialog(this, "��ǰλ���������ӣ����������ӣ�");
				}
				this.repaint();
			}
		}

		// �����ʼ����Ϸ��ť
		if (x >= 530 && x <= 630 && y >= 100 && y <= 130) {
			int result = JOptionPane.showConfirmDialog(this, "��ʼ����Ϸ���������ӽ���գ�ȷ�Ͽ�ʼ����Ϸ��");
			if (JOptionPane.OK_OPTION == result) {
				clearAllCheese();
				thread.resume();
			}
		}
		// �����Ϸ���ð�ť
		if (x >= 530 && x <= 630 && y >= 170 && y <= 200) {
			String input = JOptionPane.showInputDialog("��������Ϸʱ�䣨��λ�����ӣ�������0��ʾû��ʱ�����ƣ�");
			try {
				maxTime = Integer.parseInt(input) * 60;
				while (maxTime < 0) {
					JOptionPane.showMessageDialog(this, "��������ȷ��Ϣ�����������븺����");
					input = JOptionPane.showInputDialog("��������Ϸʱ�䣨��λ�����ӣ�������0��ʾû��ʱ�����ƣ�");
					maxTime = Integer.parseInt(input) * 60;
				}
				if (maxTime == 0) {
					int result = JOptionPane.showConfirmDialog(this, "�������,�Ƿ����¿�ʼ��Ϸ��");
					if (JOptionPane.OK_OPTION == result) {
						clearAllCheese();
						//thread.resume();
					}
				}
				if (maxTime > 0) {
					int result = JOptionPane.showConfirmDialog(this, "�������,�Ƿ����¿�ʼ��Ϸ��");
					if (JOptionPane.OK_OPTION == result) {
						clearAllCheese();
						thread.resume();
					}
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "������Ч�������ԣ�");
			}
			
		}
		// �����Ϸ˵����ť
		if (x >= 530 && x <= 600 && y >= 240 && y <= 270) {
			JOptionPane.showMessageDialog(this, "����һ����������Ϸ����,�ڰ�˫����������,��ĳһ������5��ʱ��ʤ","��Ϸ���",JOptionPane.INFORMATION_MESSAGE);
		}
		// ������䰴ť
		if (x >= 530 && x <= 630 && y >= 350 && y <= 380) {
			int result = JOptionPane.showConfirmDialog(this, "ȷ�����䣿");
			if (JOptionPane.OK_OPTION == result) {
				if (isBlack) {
					JOptionPane.showMessageDialog(this, "�ڷ������䣬�׷���ʤ��\n��Ϸ������");
				}else{
					JOptionPane.showMessageDialog(this, "�׷������䣬�ڷ���ʤ��\n��Ϸ������");
				}
				canPlay = false;
				thread.suspend();
//				allChess = new int[19][19];
//				clearAllCheese();
				this.repaint();
			}
		}
		// ������ڰ�ť
		if (x >= 530 && x <= 630 && y >= 420 && y <= 450) {
			JOptionPane.showMessageDialog(this,"����Ϸ��Jason_Lee��������ӭ����https://github.com/JasonLGJ","������Ϸ",JOptionPane.PLAIN_MESSAGE);
		}
		// ����˳���ť
		if (x >= 530 && x <= 630 && y >= 490 && y <= 520) {
			gameExit();
		}
		// ����������ְ�ť
		if (x >= 530 && y <=630 && y >= 300 && y <= 330) {
			musicOn = !musicOn;
			if (!musicOn) {
				bgMusicThread.suspend();
			}else {
				bgMusicThread.resume();
			}
			this.repaint();
		}
		
	}

	/**
	 * ��Ϸ�˳�
	 */
	private void gameExit() {
		JOptionPane.showMessageDialog(this, "�ټ���", "��Ϸ�˳�",JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}

	/**
	 * ������̣����»�ͼ
	 */
	@SuppressWarnings("deprecation")
	private void clearAllCheese() {
		/* 1.������̣�allChess[][]��ά��������ȫ����Ϊ0 */
		canPlay = true;
		// ��ʽһ��
		for(int i = 0;i < 19;i++){
			for(int j = 0;j < 19;j++){
				allChess[i][j] = 0;
			}
		}
		// ��ʽ����
//		allChess = new int[19][19]; // ��������
		
		/* 2.����Ϸ��Ϣ����ʾ�Ļص�"�ڷ�����"  */
		message = "�ڷ�����";
		
		/* 3.����һ��������˸�Ϊ�ڷ� */
		isBlack = true;
		balckTime = whiteTime = maxTime;
		balckMessage = whiteMessage = getFormatedTime(maxTime);
		
		//thread.resume(); // ���������߳�
		this.repaint(); // ���»�ͼ
	}

	/**
	 * �ж���Ӯ,һ����4������ˮƽ����ֱ�������Խ���
	 */
	private boolean checkWin() {
		
		boolean flag = false;
		int count = 1; // ����һ���ж��ٸ���ͬ��ɫ������������Ĭ��ֵ��1�����Լ���һ��
		
		/* ˮƽ�������Ƿ���5�������������ص��Ǵ�ֱ�����������ͬ��allCheese[x][y]��yֵ��ͬ */
		int color = allChess[x][y]; // ��ǰ���µ������Ǻ�ɫ���ǰ�ɫ��
		
		/*
		 * ����ж��ǲ�����ģ���Ϊһֱ�ж���ȥ�������޷��˳�
			if (color == allChess[x+1][y]) {
				count++;
				if (color == allChess[x+2][y]) {
					count++;
					if (color == allChess[x+3][y]) {
						count++;
					}
				}
			}
		*/
		
		/* ͨ��ѭ�����ж������Ƿ��������ж� */
		
		/*
		 * �ⲿ�ִ�������Ⱥܴ󡪡�ͬ���Ĵ���д��4��
			// �����ж�
			int i = 1;
			while (color==allChess[x+i][y]) { // �����ж� ---> allChess[x+i][y+0]
				count++;
				i++;
			}
			i = 1; // i��λ
			while (color == allChess[x-i][y]) { // �����ж�---> allChess[x-i][y+0]
				count++;
				i++;
			}
			if (count >= 5) {
				flag = true;
			}
			
			// �����жϣ��ͺ�����ж�һ����
			int i2 = 1;
			int count2 = 1;
			while (color==allChess[x][y+i2]) { // �����ж�  ---> allChess[x+0][y+i2]
				count2++;
				i2++;
			}
			i2 = 1; // i��λ
			while (color == allChess[x][y-i2]) { // �����ж� ---> allChess[x+0][y-i2]
				count2++;
				i2++;
			}
			if (count2 >= 5) {
				flag = true;
			}
			
			// ����-�����ж�
			int i3 = 1;
			int count3 = 1;
			while (color==allChess[x+i3][y-i3]) { // ����--->����---> allChess[x+i3][y-i3]
				count3++;
				i3++;
			}
			i3 = 1; // i3��λ
			while (color == allChess[x-i3][y+i3]) { // ����--->����---> allChess[x-i3][y+i3]
				count3++;
				i3++;
			}
			if (count3 >= 5) {
				flag = true;
			}
			
			// ����-�����ж�
			int i4 = 1;
			int count4 = 1;
			while (color==allChess[x+i4][y+i4]) { // ����--->����---> allChess[x+i4][y+i4]
				count4++;
				i4++;
			}
			i4 = 1; // i3��λ
			while (color == allChess[x-i4][y-i4]) { // ����--->����---> allChess[x-i4][y-i4]
				count4++;
				i4++;
			}
			if (count4 >= 5) {
				flag = true;
			}
		*/
		
		/* �����µĴ����ж�4����������������˺ܶ� */
		count = checkCount(1, 0, color); // �����������
		if (count >= 5) {
			flag = true;
		}else {
			count = checkCount(0, 1, color); // �����������
			if (count >= 5) {
				flag = true;
			}else {
				count = checkCount(1, -1, color); // ����-���ϵ�������
				if (count >= 5) {
					flag = true;
				}else{
					count = checkCount(1, 1, color); // ����-���µ�������
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		
		return flag;
	}

	/**
	 * �õ���ͬ���������ӵ�����,�������4���жϳ��������
	 * @param xChange:x�ı仯
	 * @param yChange��y�ı仯
	 * @param color����ǰ����״̬���ڣ��ף�
	 * @return
	 */
	private int checkCount(int xChange,int yChange,int color){
		
		int count = 1;
		int tempX = xChange, tempY = yChange; // ���洫������xChange��yChange����λ��ʱ����Ҫ�õ�
		// ����Ҫ��������±��Ƿ�Խ��
		while (x + xChange >= 0 && x + xChange <= 18 && y + yChange >= 0 && y + yChange <= 18 && color == allChess[x + xChange][y + yChange]) {
			count++;
			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0) {
					yChange++;
				}else {
					yChange--;
				}
			}
		}
		
		xChange = tempX; // ��λ
		yChange = tempY;
		// ����Ҫ��������±��Ƿ�Խ��
		while (x - xChange >= 0 && x - xChange <= 18 && y - yChange >= 0 && y - yChange <= 18 && color == allChess[x - xChange][y - yChange]) {
			count++;
			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0) {
					yChange++;
				}else {
					yChange--;
				}
			}
		}
		
		return count;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void run() {

		// �Ƿ���ʱ������ƣ�
		if (maxTime > 0) {
			while (true) {
				
				if (isBlack) {
					balckTime--;
				}else {
					whiteTime--;
				}
				
				if (whiteTime == 0) {
					JOptionPane.showMessageDialog(this, "�׷���ʱ���ڷ���ʤ��");
					canPlay = false;
				}
				if (balckTime == 0) {
					JOptionPane.showMessageDialog(this, "�ڷ���ʱ���׷���ʤ��");
					canPlay = false;
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("�ڷ�ʱ��\t�׷�ʱ��  ����λ���롿");
				System.out.println(balckTime + "\t" + whiteTime);
				
				balckMessage = getFormatedTime(balckTime);
				whiteMessage = getFormatedTime(whiteTime);
				this.repaint();
				
			}
		}
	}

	/**
	 * ���ظ�ʽ����ʱ���뼴��hh:MM:ss
	 * @param second
	 * @return
	 */
	private static String getFormatedTime(int second){
		
		int currentSecond = second % 60;
		int totalMinute = second / 60;
		int currentMinute = totalMinute % 60;
		int totalHour = totalMinute / 60;
		return checkTime(totalHour) + ":" + checkTime(currentMinute) + ":" + checkTime(currentSecond);
	}
	
	/**
	 * ���ʱ��С��1λ����ǰ�油0
	 * @param number
	 * @return
	 */
	private static String checkTime(int number){
		
		String result = "";
		if (number < 0) {
			result = "00";
		}else {
			result = number <= 9? "0" + number: "" + number;
		}
		return result;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();

		if (x >= 15 && x <= 465 && y >= 70 && y <= 520) {
			 //���ϵ��ж����������x������ڵ�Ԫ������ߣ�����Ӧ�������Ҳ��ʮ���ߣ�y����ͬ�� 
			if (((x - 15) % 25) >= 13) {
				x = ((x - 15) / 25) + 1;
			} else
				x = (x - 15) / 25;
			if (((y - 70) % 25) >= 13) {
				y = ((y - 70) / 25) + 1;
			} else
				y = (y - 70) / 25;

			cursor.point.x = 15 + x * 25;
			cursor.point.y = 70 + y * 25;
			cursor.show = true;
		}else {
			cursor.show = false;
		}
		repaint();
	}
}