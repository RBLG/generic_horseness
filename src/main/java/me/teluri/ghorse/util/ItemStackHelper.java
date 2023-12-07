package me.teluri.ghorse.util;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public final class ItemStackHelper {
	private ItemStackHelper() {
	}

	public static String getIdString(ItemStack source) {
		return Registries.ITEM.getId(source.getItem()).toString();
	}

	public static void decrementIfFinite(ItemStack source, boolean isinfinite, int quantity) {
		if (!isinfinite) {
			source.decrement(quantity);
		}
	}

	public static ItemStack getOneFrom(ItemStack source, boolean isinfinite) {
		ItemStack rtn = source.copyWithCount(1);
		decrementIfFinite(source, isinfinite, 1);
		return rtn;
	}

	public static ItemStack getUpToFrom(ItemStack source, boolean isinfinite, int quantity) {
		quantity = Integer.min(quantity, source.getCount());
		ItemStack rtn = source.copyWithCount(quantity);
		decrementIfFinite(source, isinfinite, quantity);
		return rtn;
	}

	public static void uncheckedPassFromTo(ItemStack source, ItemStack target, int quantity, boolean isinfinite) {
		decrementIfFinite(source, isinfinite, quantity);
		target.increment(quantity);
	}

	public static void checkedPassFromTo(ItemStack source, ItemStack target, int quantity, boolean isinfinite, int targetMaxCount) {
		if (!ItemStack.areItemsEqual(source, target)) {
			return;
		}
		quantity = Integer.min(quantity, source.getCount());
		quantity = Integer.min(quantity, targetMaxCount);
		uncheckedPassFromTo(source, target, quantity, isinfinite);
	}

}
