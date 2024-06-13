package com.raven.chess;

import javax.swing.JFrame;

public class ChessController {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Chess");
		frame.setSize(600, 600);
		ChessView chess = new ChessView();
		frame.add(chess);
		frame.setVisible(true);
	}

}
