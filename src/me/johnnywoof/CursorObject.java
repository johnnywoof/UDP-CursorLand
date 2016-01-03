package me.johnnywoof;

import java.util.Arrays;

public class CursorObject {

	private final byte[] uuid;
	public int x = 0, y = 0;
	public long lastUpdate = System.currentTimeMillis();

	public CursorObject(byte[] uuid) {
		this.uuid = uuid;
	}

	public boolean isUUID(byte[] uuid) {
		return Arrays.equals(this.uuid, uuid);
	}

	@Override
	public boolean equals(Object o) {

		return o == this || o instanceof CursorObject && Arrays.equals(((CursorObject) o).uuid, this.uuid);

	}

}
