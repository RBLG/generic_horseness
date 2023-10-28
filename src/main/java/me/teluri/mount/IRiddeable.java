package me.teluri.mount;

import me.teluri.util.IEntityProvider;
import me.teluri.util.ISideAware;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public interface IRiddeable extends ISideAware, IEntityProvider {

	boolean canRide();

	default ActionResult evaluateRideAction(ItemStack stack, boolean isinfinite, PlayerEntity player) {
		if (canRide()) {
			this.getRiddenBy(player);
			ActionResult.success(isClient());
		}
		return ActionResult.PASS;
	}

	Entity getRiddeable();

	default void getRiddenBy(PlayerEntity player) {
		Entity self = getRiddeable();
		if (!isClient()) {
			player.setYaw(self.getYaw());
			player.setPitch(self.getPitch());
			player.startRiding(self);
		}
	}
}
