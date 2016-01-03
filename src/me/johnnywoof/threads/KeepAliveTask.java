package me.johnnywoof.threads;

import me.johnnywoof.CursorLand;
import me.johnnywoof.Start;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.TimerTask;

public class KeepAliveTask extends TimerTask {

	private final CursorLand cursorLand;
	private final DatagramPacket keepAlivePacket;

	public KeepAliveTask(CursorLand cursorLand) {
		this.cursorLand = cursorLand;

		//A short is 2 bytes
		//A long is 8 bytes
		//The uuid is 16 bytes

		this.keepAlivePacket = new DatagramPacket(
				ByteBuffer.allocate(CursorLand.DATA_LENGTH).putShort((short) 1).put(this.cursorLand.uuid).putLong(System.currentTimeMillis()).array(),
				CursorLand.DATA_LENGTH, this.cursorLand.group, Start.PORT_NUMBER
		);

	}

	@Override
	public void run() {

		try {
			this.cursorLand.socket.send(this.keepAlivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
