package me.teluri.ghorse.util;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public final class InventoryHelper {

	private InventoryHelper() {

	}

	public static SimpleInventory copyToNewAndDropRemaining(SimpleInventory source, int size, Entity ent) {
		SimpleInventory target = new SimpleInventory(size);
		copyAndDropRemaining(source, target, ent);
		return target;
	}

	public static void copyAndDropRemaining(SimpleInventory source, SimpleInventory target, Entity ent) {
		int copyablelength = Math.min(source.size(), target.size());
		for (int iter1 = 0; iter1 < copyablelength; iter1++) {
			ItemStack itemStack = source.getStack(iter1);
			if (!itemStack.isEmpty()) {
				target.setStack(iter1, itemStack.copy());
			}
		}
		if (target.size() < source.size()) {
			for (int iter2 = copyablelength; iter2 < source.size(); iter2++) {
				ItemStack itemStack = source.getStack(iter2);
				if (!itemStack.isEmpty()) {
					ent.dropStack(itemStack);
				}
			}
		}
	}

	public static void dropAll(SimpleInventory source, Entity ent) {
		for (int iter2 = 0; iter2 < source.size(); iter2++) {
			ItemStack itemStack = source.getStack(iter2);
			if (!itemStack.isEmpty()) {
				ent.dropStack(itemStack);
			}
		}
	}

	public static boolean isSlotAvailable(SimpleInventory inv, int slot) {
		return inv.getStack(slot).isEmpty();

	}
}
