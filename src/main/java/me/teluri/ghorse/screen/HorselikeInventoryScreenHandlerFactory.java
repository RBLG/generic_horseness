package me.teluri.ghorse.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HorselikeInventoryScreenHandlerFactory implements ExtendedScreenHandlerFactory {

	@Override
	public Text getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScreenHandler createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

}
