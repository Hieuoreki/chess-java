package com.raven.chess;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChessController implements ChessDelegate, ActionListener, Runnable {
	
	private JFrame frame;
	
	private ChessModel chessModel = new ChessModel();
	private ChessView chessBoardPanel;
	private JButton btnReset;
	private JButton btnServer;
	private JButton btnClient;
	
	private PrintWriter printWriter;
	private Scanner scanner;
	
	public ChessController() {
		// TODO Auto-generated constructor stub
		chessModel.reset();
		
		frame = new JFrame("Chess");
		frame.setSize(600, 600);
//		frame.setLocation(0, 1300);
		frame.setLayout(new BorderLayout());
		
		chessBoardPanel = new ChessView(this);
		
		frame.add(chessBoardPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnReset = new JButton("Reset");
		btnReset.addActionListener(this);
		buttonsPanel.add(btnReset);
		
		btnServer = new JButton("Listen");
		btnServer.addActionListener(this);
		buttonsPanel.add(btnServer);
		
		btnClient = new JButton("Connect");
		btnClient.addActionListener(this);
		buttonsPanel.add(btnClient);
		
		frame.add(buttonsPanel, BorderLayout.PAGE_END);
		
		
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
		chessBoardPanel.repaint();
		if(printWriter != null)
		{
			printWriter.println(fromCol + "," + fromRow + "," + toCol + "," + toRow);
		}
	}
	
	private void receiveMove()
	{
		while(scanner.hasNextLine())
		{
			String moveStr = scanner.nextLine();
			System.out.println("server " + moveStr);
			String[] moveStrAll = moveStr.split(",");
			int fromCol = Integer.parseInt(moveStrAll[0]);
			int fromRow = Integer.parseInt(moveStrAll[1]);
			int toCol = Integer.parseInt(moveStrAll[2]);
			int toRow = Integer.parseInt(moveStrAll[3]);
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					chessModel.movePiece(fromCol, fromRow, toCol, toRow);
					chessBoardPanel.repaint();
				}
			});
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnReset)
		{
			chessModel.reset();
			chessBoardPanel.repaint();
		}
		else if(e.getSource() == btnServer)
		{
			frame.setTitle("Chat Server");
			ExecutorService  pool = Executors.newFixedThreadPool(1);
			pool.execute(this);
		}
		else if(e.getSource() == btnClient)
		{
			frame.setTitle("Chat Client");
			System.out.println("connect for socket client");
			try {
				if(scanner == null || printWriter == null)
				{
					Socket socket = new Socket("localhost", 5000);
					scanner = new Scanner(socket.getInputStream());
					printWriter = new PrintWriter(socket.getOutputStream());
				}
				Executors.newFixedThreadPool(1).execute(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						receiveMove();
					}
				});
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket listener = new ServerSocket(5000);
			System.out.println("server is listening on port 5000");
			
			if(scanner == null || printWriter == null)
			{
				Socket socket = listener.accept();
				printWriter = new PrintWriter(socket.getOutputStream(), true);
				scanner = new Scanner(socket.getInputStream());
			}
			receiveMove();
			
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}

}
