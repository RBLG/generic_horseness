package me.teluri.ghorse.util;

import net.minecraft.entity.Entity;

public final class EntityHelper {
	private EntityHelper() {

	}
	
	//abstract the getworld part
	public static boolean isClient(Entity ent) {
		return ent.getWorld().isClient();
	}
}
