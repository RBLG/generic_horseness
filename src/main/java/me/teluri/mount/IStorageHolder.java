package me.teluri.mount;

import me.teluri.util.IHasInventory;
import me.teluri.util.ISideAware;
import me.teluri.util.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface IStorageHolder extends ISideAware, IHasInventory {

	boolean isStorageSlotAvailable(ItemStack stack);

	boolean isFittingStorage(ItemStack stack);

	int getChestSlot();

	default boolean canEquipStorage(ItemStack stack) {
		return isFittingStorage(stack) && isStorageSlotAvailable(stack);
	}

	default ActionResult evaluateEquipStorageAction(ItemStack stack, boolean isinfinite) {
		if (canEquipStorage(stack)) {
			getInventory().setStack(getChestSlot(), ItemStackHelper.getOneFrom(stack, isinfinite));
			ActionResult.success(isClient());
		}
		return ActionResult.PASS;
	}
}
