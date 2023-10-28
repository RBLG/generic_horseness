package me.teluri.ghorse.mount;

import me.teluri.ghorse.util.IHasInventory;
import me.teluri.ghorse.util.ISideAware;
import me.teluri.ghorse.util.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface IArmorable extends ISideAware,IHasInventory {

	boolean isArmorSlotAvailable(ItemStack stack);

	boolean isFittingArmor(ItemStack stack);

	int getArmorSlot();

	default boolean canEquipArmor(ItemStack stack) {
		return isFittingArmor(stack) && isArmorSlotAvailable(stack);
	}

	default ActionResult evaluateArmorEquipAction(ItemStack stack, boolean isinfinite) {
		if (canEquipArmor(stack)) {
			getInventory().setStack(getArmorSlot(), ItemStackHelper.getOneFrom(stack, isinfinite));
			return ActionResult.success(isClient());
		}
		return ActionResult.PASS;
	}

}
