package me.teluri.ghorse.mount;

import me.teluri.ghorse.util.IHasInventory;
import me.teluri.ghorse.util.ISideAware;
import me.teluri.ghorse.util.ItemStackHelper;
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
