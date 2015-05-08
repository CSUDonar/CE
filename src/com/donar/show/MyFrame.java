package com.donar.show;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.donar.alg.Graph;

public class MyFrame extends JFrame {
	public MyFrame(Graph graph1,int x, int y,String title) {
		setTitle(title);
		setVisible(true);
		setLocation(x, y);
		setSize(420, 420);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setSize(404, 382);
		panel.setLayout(null);
		
		GraphPaint paint =new GraphPaint(graph1);
		paint.setBounds(0, 0, 404, 380);
		paint.width = 400;
		paint.height = 400;
		panel.add(paint);
		paint.init();
		panel.setLocation(0, 0);
	}
}
