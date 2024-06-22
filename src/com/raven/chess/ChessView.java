package com.raven.chess;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessView extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L; 
	
	private double scareFactor = 0.9;
	private int originX = -1;
	private int originY = -1;
	private int cellSize = -1;
	
	private ChessDelegate chessDelegate;
	
	// Lưu trữ ảnh
	private Map<String, Image> keyNameValueImage = new HashMap<String, Image>();
	private int fromCol = -1;
	private int fromRow = -1;
	
	private ChessPiece movingPiece;
	private Point movingPointPiece;
	
	ChessView(ChessDelegate chessDelegate)
	{
		this.chessDelegate = chessDelegate;
		String[] images = {
				ChessContants.bBishop,
				ChessContants.wBishop,
				ChessContants.bKing,
				ChessContants.wKing,
				ChessContants.bKnight,
				ChessContants.wKnight,
				ChessContants.bPawn,
				ChessContants.wPawn,
				ChessContants.bQueen,
				ChessContants.wQueen,
				ChessContants.bRook,
				ChessContants.wRook,
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
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintChildren(Graphics g) {
		// TODO Auto-generated method stub
		super.paintChildren(g);
		
		int smaller = Math.min(getSize().width, getSize().height);
		cellSize= (int)(((double)smaller)* scareFactor/8);
		originX = (getSize().width - 8 * cellSize)/2;
		originY = (getSize().height - 8 * cellSize)/2;
		
		Graphics2D g2 = (Graphics2D)g;
		
		drawBoard(g2);
		
		drawPieces(g2);
		
	}
	
	public void drawPieces(Graphics2D g2)
	{
		for (int row = 0; row < 8; row++) 
		{
			for (int col = 0; col < 8; col++) 
			{
				ChessPiece p = chessDelegate.pieceAt(col, row);
				if(p != null && p != movingPiece)
				{
					drawImage(g2, col, row, p.imgName);
				}
			}
		}
		
		if(movingPiece != null && movingPointPiece != null)
		{
			Image img = keyNameValueImage.get(movingPiece.imgName);
			g2.drawImage(img, movingPointPiece.x - cellSize/2, movingPointPiece.y - cellSize/2, cellSize, cellSize, null);
		}
	}
	
	// Phương thức hiện 1 quân cờ trong 1 ô
	private void drawImage(Graphics2D g2, int col, int row, String image)
	{
		Image img = keyNameValueImage.get(image);
		g2.drawImage(img, originX + col * cellSize, originY + ( 7 - row ) * cellSize, cellSize, cellSize, null);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		fromCol = (e.getPoint().x - originX) / cellSize;
		fromRow = 7 - (e.getPoint().y - originY) / cellSize;
		movingPiece = chessDelegate.pieceAt(fromCol, fromRow);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		int col = (e.getPoint().x - originX) / cellSize;
		int row = 7 - (e.getPoint().y - originY) / cellSize;
		
		if(fromCol != col || fromRow != row)
		{
			chessDelegate.movePiece(fromCol, fromRow, col, row);
		}
				
		movingPiece = null;
		movingPointPiece = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		movingPointPiece = e.getPoint();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
