package me.teluri.ghorse.util;

import net.minecraft.item.ItemStack;

public final class ItemStackHelper {
	private ItemStackHelper() {
	}

	public static ItemStack getOneFrom(ItemStack source, boolean isinfinite) {
		ItemStack rtn = source.copyWithCount(1);
		if (!isinfinite) {
			source.decrement(1);
		}
		return rtn;
	}

}
