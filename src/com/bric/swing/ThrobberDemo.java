package com.bric.swing;

import com.bric.plaf.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

//todo add copyright infos for jeremy woods (@email)
public class ThrobberDemo {

	public static void main(String[] args) {
				
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		panel.setLayout(null);
		
		frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        
		JThrobber throbber = new JThrobber();
		throbber.setUI(new AquaThrobberUI());
		
		throbber.setBounds(100,100, 100, 100);
		panel.add(throbber);
		
		
		frame.setVisible(true);
	}

	
	
	
}
