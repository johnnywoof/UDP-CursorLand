package me.johnnywoof;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Start {

	public static boolean RUNNING = true;
	public static final int PORT_NUMBER = 4238;
	public static final String GROUP_ADDRESS = "230.2.4.1";

	public static void main(String[] args) throws IOException {

		JFrame frame = new JFrame("Cursor Land");

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final CursorLand cursorLand = new CursorLand();

		frame.add(cursorLand, BorderLayout.CENTER);

		frame.pack();

		frame.setSize(500, 400);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				RUNNING = false;
				cursorLand.stop();
			}
		});

		cursorLand.start();

	}

}
