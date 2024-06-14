package com.raven.chess;

import javax.swing.JFrame;

public class ChessController implements ChessDelegate {
	
	private ChessModel chessModel = new ChessModel();
	private ChessView chessView;
	
	public ChessController() {
		// TODO Auto-generated constructor stub
		chessModel.reset();
		
		JFrame frame = new JFrame("Chess");
		frame.setSize(600, 600);
//		frame.setLocation(0, 1300);
		
		chessView = new ChessView(this);
		
		frame.add(chessView);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new ChessController();
	}

	@Override
	public ChessPiece pieceAt(int col, int row) {
		// TODO Auto-generated method stub
		return chessModel.pieceAt(col, row);
	}

	@Override
	public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
		// TODO Auto-generated method stub
		chessModel.movePiece(fromCol, fromRow, toCol, toRow);
		chessView.repaint();
	}

}
