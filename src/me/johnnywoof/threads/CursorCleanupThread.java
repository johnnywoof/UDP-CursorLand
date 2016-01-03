package me.johnnywoof.threads;

import me.johnnywoof.CursorLand;
import me.johnnywoof.CursorObject;

import java.util.TimerTask;

public class CursorCleanupThread extends TimerTask {

	private final CursorLand cursorLand;

	public CursorCleanupThread(CursorLand cursorLand) {
		this.cursorLand = cursorLand;
	}

	@Override
	public void run() {

		for (CursorObject c : this.cursorLand.cursors) {

			if ((System.currentTimeMillis() - c.lastUpdate) > 1000) {

				this.cursorLand.cursors.remove(c);
				this.cursorLand.repaint();

			}

		}

	}

}
