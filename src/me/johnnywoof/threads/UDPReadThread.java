package me.johnnywoof.threads;

import me.johnnywoof.CursorLand;
import me.johnnywoof.Start;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPReadThread extends Thread {

	private final CursorLand cursorLand;

	public UDPReadThread(CursorLand cursorLand) {
		this.cursorLand = cursorLand;
	}

	@Override
	public void run() {

		try {

			while (Start.RUNNING) {

				byte[] data = new byte[CursorLand.DATA_LENGTH];
				this.cursorLand.socket.receive(new DatagramPacket(data, CursorLand.DATA_LENGTH));

				this.cursorLand.onPacket(data);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
