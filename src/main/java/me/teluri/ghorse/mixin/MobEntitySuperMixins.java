package me.teluri.ghorse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MobEntitySuperMixins extends LivingEntity {

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true) // TODO HACK, ADD MODID TO SIG
	public void interactMobMixinHead(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		interactMobMixinHeadHolder(player, hand, info);
	}

	public void interactMobMixinHeadHolder(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {

	}

	@Inject(method = "interactMob", at = @At("RETURN"), cancellable = true) // TODO HACK, ADD MODID TO SIG
	public void interactMobMixinTail(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		interactMobMixinTailHolder(player, hand, info);
	}

	protected void interactMobMixinTailHolder(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {

	}

	// java fakery
	protected MobEntitySuperMixins(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
}
