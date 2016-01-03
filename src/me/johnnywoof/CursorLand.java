package me.johnnywoof;

import me.johnnywoof.threads.CursorCleanupThread;
import me.johnnywoof.threads.UDPReadThread;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class CursorLand extends JPanel {

	public static final int DATA_LENGTH = 24;

	public final MulticastSocket socket;
	public final InetAddress group;
	private final byte[] uuid;

	private long lastSent = 0;

	private final BufferedImage cursorImg;

	public final Set<CursorObject> cursors = new CopyOnWriteArraySet<>();

	private final java.util.Timer cleanupTimer = new Timer();

	public CursorLand() throws IOException {

		InputStream in = this.getClass().getResourceAsStream("cursor.png");

		this.cursorImg = ImageIO.read(in);

		in.close();

		this.socket = new MulticastSocket(Start.PORT_NUMBER);

		UUID uuidObj = UUID.randomUUID();
		this.uuid = ByteBuffer.allocate(16).putLong(uuidObj.getMostSignificantBits()).putLong(uuidObj.getLeastSignificantBits()).array();

		this.group = InetAddress.getByName(Start.GROUP_ADDRESS);

		this.socket.joinGroup(this.group);

		this.addMouseMotionListener(this.getMouseListener());

	}

	public MouseMotionAdapter getMouseListener() {
		return new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				CursorLand.this.sendPosition(e.getX(), e.getY());
			}
		};
	}

	public void onPacket(byte[] data) {

		//System.out.println("Packet income");

		byte[] uuid = Arrays.copyOf(data, 16);

		if (Arrays.equals(uuid, this.uuid))
			return;

		for (CursorObject c : this.cursors) {
			if (c.isUUID(uuid)) {
				ByteBuffer cords = ByteBuffer.wrap(Arrays.copyOfRange(data, 16, 24));
				int x = cords.getInt(0);
				int y = cords.getInt(4);
				//System.out.println("Position GET - " + x + " : " + y);
				c.x = x;
				c.y = y;
				c.lastUpdate = System.currentTimeMillis();
				this.repaint();
				return;
			}
		}

		//System.out.println("UUID not found. Creating a new cursor");

		this.cursors.add(new CursorObject(uuid));

	}

	protected void sendPosition(int x, int y) {
		//16 bytes = our uuid
		//4 bytes = x value
		//4 bytes = y value

		if ((System.currentTimeMillis() - this.lastSent) > 100) {

			this.lastSent = System.currentTimeMillis();

			//System.out.println("Sending position - " + x + ":" + y + " | " + this.byteCounter);

			byte[] data = ByteBuffer.allocate(DATA_LENGTH).put(this.uuid).putInt(x).putInt(y).array();

			try {
				this.socket.send(new DatagramPacket(data, DATA_LENGTH, this.group, Start.PORT_NUMBER));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.CYAN);

		for (CursorObject c : this.cursors)
			g.drawImage(this.cursorImg, c.x, c.y, this.cursorImg.getWidth(), this.cursorImg.getHeight(), this);

		g.dispose();
	}

	public void start() {
		this.cleanupTimer.scheduleAtFixedRate(new CursorCleanupThread(this), 0, 1000);
		new UDPReadThread(this).start();
	}

	public void stop() {
		try {
			this.socket.leaveGroup(this.group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cleanupTimer.cancel();
	}

}
