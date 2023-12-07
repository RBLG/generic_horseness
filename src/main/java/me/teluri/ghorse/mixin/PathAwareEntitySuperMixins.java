package me.teluri.ghorse.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

@Mixin(PathAwareEntity.class)
public abstract class PathAwareEntitySuperMixins extends MobEntitySuperMixins {

	//java fakery
	protected PathAwareEntitySuperMixins(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

}
