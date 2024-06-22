package com.raven.chess;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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

public class ChessController implements ChessDelegate, ActionListener {
	
	private int PORT = 50000;
	
	private JFrame frame;
	
	private ChessModel chessModel = new ChessModel();
	private ChessView chessBoardPanel;
	private JButton btnReset;
	private JButton btnServer;
	private JButton btnClient;
	
	private Socket socket;
	private ServerSocket listener;
	
	private PrintWriter printWriter;
	
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
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if(printWriter != null) printWriter.close();
				try {
					if(listener != null) listener.close();
					if(socket != null) socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
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
	
	private void receiveMove(Scanner scanner)
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
	
	private void runSocketServer()
	{
		Executors.newFixedThreadPool(1).execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					listener = new ServerSocket(PORT);
					System.out.println("server is listening on port: " + PORT);

					socket = listener.accept();
					printWriter = new PrintWriter(socket.getOutputStream(), true);
					Scanner scanner = new Scanner(socket.getInputStream());
					
					receiveMove(scanner);
					
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		});
	}
	
	private void runSocketClient()
	{
		try {
			socket = new Socket("localhost", PORT);
			Scanner scanner = new Scanner(socket.getInputStream());
			printWriter = new PrintWriter(socket.getOutputStream(), true);

			Executors.newFixedThreadPool(1).execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					receiveMove(scanner);
				}
			});
			
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnReset)
		{
			chessModel.reset();
			chessBoardPanel.repaint();
			
			try {
				if(listener != null)
				{
					listener.close();
				}
				if(socket != null)
				{
					socket.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			btnClient.setEnabled(true);
			btnServer.setEnabled(true);
		}
		else if(e.getSource() == btnServer)
		{
			btnServer.setEnabled(false); // Vô hiệu hóa sau 1 lần bấm
			btnClient.setEnabled(false);
			frame.setTitle("Chat Server");
			runSocketServer();
		}
		else if(e.getSource() == btnClient)
		{	
			btnClient.setEnabled(false);
			btnServer.setEnabled(false);
			frame.setTitle("Chat Client");
			System.out.println("connect for socket client");
			runSocketClient();
		}
	}



}
