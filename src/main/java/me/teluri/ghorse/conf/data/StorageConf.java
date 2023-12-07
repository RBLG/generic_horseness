package me.teluri.ghorse.conf.data;

public class StorageConf {
	public static final String IDENTIFIER = "item";
	public final int amount;
	public final int width;
	public final int height;

	public StorageConf(int namount, int nwidth, int nheight) {
		amount = namount;
		width = nwidth;
		height = nheight;
	}

	public int getSize() {
		return width * height;
	}

}
