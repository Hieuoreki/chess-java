package com.raven.chess;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	int originX = 55;
	int originY = 45;
	int cellSize = 60;
	
	ChessDelegate chessDelegate;
	
	// Lưu trữ ảnh
	Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
	
	public ChessView()
	{
		String[] images = {
				"Bishop-black",
				"Bishop-white",
				"King-black",
				"King-white",
				"Knight-black",
				"Knight-white",
				"Pawn-black",
				"Pawn-white",
				"Queen-black",
				"Queen-white",
				"Rook-black",
				"Rook-white",
		};
		
		try {
			for(String image : images)
			{
				Image img = loadImage(image + ".png");
				keyNameValueImage.put(image, img);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintChildren(Graphics g) {
		// TODO Auto-generated method stub
		super.paintChildren(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		drawBoard(g2);
		
		drawPieces(g2);
		
	}
	
	public void drawPieces(Graphics2D g2)
	{
		for (int row = 7; row >= 0; row--) 
		{
			for (int col = 0; col < 8; col++) 
			{
				ChessPiece p = chessDelegate.pieceAt(col, row);
				if(p != null)
				{
					drawImage(g2, col, row, p.imgName);
				}
			}
		}
	}
	
	// Phương thức hiện 1 quân cờ trong 1 ô
	private void drawImage(Graphics2D g2, int col, int row, String image)
	{
		Image img = keyNameValueImage.get(image);
		g2.drawImage(img, originX + col * cellSize, originY + row * cellSize, cellSize, cellSize, null);
	}
	
	// Phương thức load 1 ảnh cờ
	private Image loadImage(String fileName) throws Exception{
		
			ClassLoader classLoader = getClass().getClassLoader();
			URL resUrl = classLoader.getResource("img/" + fileName);
			if(resUrl == null)
			{
				System.out.println("none");
				return null;
			}
			else
			{
				File imgFile = new File(resUrl.toURI());
				return ImageIO.read(imgFile);
			}
	}
	
	// Phương thức hiện thị các ô theo cấu trúc bàn cờ 8x8
	private void drawBoard(Graphics2D g2)
	{
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 4; j++) 
			{
				drawSquare(g2, 0 + 2 * j, 0 + 2 * i, true);
				drawSquare(g2, 1 + 2 * j, 1 + 2 * i, true);
				
				drawSquare(g2, 1 + 2 * j, 0 + 2 * i, false);
				drawSquare(g2, 0 + 2 * j, 1 + 2 * i, false);
			}
		}
	}
	
	// Phươnng thức định nghĩa 1 ô
	private void drawSquare(Graphics2D g2, int col, int row, boolean light)
	{
		g2.setColor(light ? Color.white : Color.gray);
		g2.fillRect(originX + col * cellSize, originY + row * cellSize, cellSize, cellSize);
	}

}
