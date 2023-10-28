package me.teluri.ghorse.mount;

import me.teluri.ghorse.screen.HorselikeInventoryScreenHandlerFactory;
import me.teluri.ghorse.util.IEntityProvider;
import me.teluri.ghorse.util.IHasInventory;
import me.teluri.ghorse.util.ISideAware;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface IHasOpenableInventory extends IHasInventory, ISideAware, IEntityProvider {

	boolean hasStorage();

	Text getInventoryName();

	default ActionResult evaluateOpenInventoryAction() {
		if (hasStorage()) {

			ActionResult.success(isClient());
		}
		return ActionResult.PASS;
	}

	default void openInventory(PlayerEntity player) {
		Entity self = getEntity();
		if (!isClient() && (!self.hasPassengers() || self.hasPassenger(player))) {
			/* OptionalInt optionalInt = */player.openHandledScreen(getScreenHandlerFactory());
		}
	}
	
	default NamedScreenHandlerFactory getScreenHandlerFactory() {
		return new HorselikeInventoryScreenHandlerFactory();
	}
}
