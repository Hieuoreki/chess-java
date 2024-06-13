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
		
		chessView = new ChessView();
		chessView.chessDelegate = this;
		
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

}
