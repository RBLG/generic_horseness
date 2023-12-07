package me.teluri.ghorse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.teluri.ghorse.GenericHorseness;
import me.teluri.ghorse.conf.data.MountConf;
import me.teluri.ghorse.conf.data.StorageConf;
import me.teluri.ghorse.screen.MountStorageScreenHandlerFactory;
import me.teluri.ghorse.util.InventoryHelper;
import me.teluri.ghorse.util.ItemStackHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.RideableInventory;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import static me.teluri.ghorse.util.EntityHelper.*;

@Mixin(PassiveEntity.class)
public abstract class PassiveEntityMountMixins extends PathAwareEntitySuperMixins implements Saddleable, RideableInventory, InventoryOwner {

	protected MountConf conf;

	// private static final TrackedData<Boolean> IS_ENABLED =
	// DataTracker.registerData
	// (PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_TRAINED = DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_SADDLED = DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_ARMORED = DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> STORAGE_AMOUNT = DataTracker.registerData(PassiveEntity.class, TrackedDataHandlerRegistry.INTEGER);

	protected SimpleInventory items;

	@Inject(method = "<init>*", at = @At("RETURN"))
	protected void onConstructorMixin(CallbackInfo info) {
		conf = GenericHorseness.getConfig(this);
		if (conf.isDisabled()) {
			return;
		}
		this.dataTracker.startTracking(IS_TRAINED, false);
		this.dataTracker.startTracking(IS_SADDLED, false);
		this.dataTracker.startTracking(IS_ARMORED, false);
		this.dataTracker.startTracking(STORAGE_AMOUNT, 0);
		this.onStorageUpdated();
	}

	protected void onStorageUpdated() {
		if (conf.isDisabled()) {
			return;
		}
		SimpleInventory old = items;
		StorageConf stoconf = conf.getStorageConf(old.getStack(MountConf.storageSlot));
		items = new SimpleInventory(3 + stoconf.getSize() * this.getStorageAmount());
		if (old != null) {
			old.removeListener(icl);
			InventoryHelper.copyAndDropRemaining(old, items, this);
		}
		items.addListener(icl);
		updateEquipmentStatus();
	}

	protected void updateEquipmentStatus() {
		if (conf.isDisabled() || isClient(this)) {
			return;
		}
		this.dataTracker.set(IS_SADDLED, !this.items.getStack(0).isEmpty());
		this.dataTracker.set(IS_ARMORED, !this.items.getStack(1).isEmpty());
		this.dataTracker.set(STORAGE_AMOUNT, this.items.getStack(2).getCount());
	}

	private final InventoryChangedListener icl = (e) -> {
		boolean wassaddled = this.isSaddled();
		this.updateEquipmentStatus();
		if (wassaddled && this.isSaddled()) {
			this.playSound(this.getSaddleSound(), 0.5f, 1.0f);
		}
	};

	public Text getInventoryName() {
		return this.getCustomName();
	}

	public NamedScreenHandlerFactory getScreenHandlerFactory() {
		return new MountStorageScreenHandlerFactory();
	}

	@Override
	public void interactMobMixinHeadHolder(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		if (conf.isDisabled() || hasPassengers() || isBaby()) {
			return;
		}
		// evaluateOpeningInventoryAction
		if (player.shouldCancelInteraction()) {
			this.openInventory(player);
			info.setReturnValue(getSuccess());
			return;
		}
	}

	@Override
	public void interactMobMixinTailHolder(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		if (info.getReturnValue().isAccepted() || hasPassengers() || isBaby() || conf.isDisabled()) {
			return;
		}
		ItemStack handStack = player.getStackInHand(hand);
		boolean isinfinite = player.isCreative();
		if (!handStack.isEmpty()) {
			// evaluateEquipingSaddleAction
			if (conf.isFittingSaddle(handStack) && isSaddleSlotAvailable()) {
				items.setStack(MountConf.saddleSlot, ItemStackHelper.getOneFrom(handStack, isinfinite));
				info.setReturnValue(getSuccess());
				return;
			}
			// evaluateEquipingStorageAction
			StorageConf storconf = conf.getStorageConf(handStack);
			if (conf.isFittingStorage(handStack)) {
				ItemStack storageStack = items.getStack(MountConf.storageSlot);
				if (storageStack.isEmpty()) {
					getInventory().setStack(MountConf.storageSlot, ItemStackHelper.getOneFrom(handStack, isinfinite));
				} else if (ItemStack.areItemsEqual(storageStack, handStack) && storageStack.getCount() < storconf.amount) {
					ItemStackHelper.uncheckedPassFromTo(handStack, storageStack, 1, isinfinite);
				}
				info.setReturnValue(getSuccess());
				return;
			}
			// evaluateEquipingArmorAction
			if (conf.isFittingArmor(handStack) && isArmorSlotAvailable()) {
				items.setStack(MountConf.armorSlot, ItemStackHelper.getOneFrom(handStack, isinfinite));
				info.setReturnValue(getSuccess());
				return;
			}
		}
		// evaluateRidingAction
		if (conf.riddeable) {
			this.putPlayerOnBack(player);
			info.setReturnValue(getSuccess());
		}
	}

	public void putPlayerOnBack(PlayerEntity player) {
		if (isClient(this) || conf.isDisabled()) {
			return;
		}
		player.setYaw(this.getYaw());
		player.setPitch(this.getPitch());
		player.startRiding(this);
	}

	private int getStorageAmount() {
		if (conf.isDisabled()) {// if disabled, storage_amount doesnt exist
			return 0;
		}
		return dataTracker.get(STORAGE_AMOUNT);
	}

	/////////////// util ///////////////////////

	private ActionResult getSuccess() {
		return ActionResult.success(isClient(this));
	}

	protected boolean isSaddleSlotAvailable() {
		return InventoryHelper.isSlotAvailable(items, MountConf.saddleSlot);
	}

	protected boolean isStorageSlotAvailable() {
		return InventoryHelper.isSlotAvailable(items, MountConf.storageSlot); // TODO handle storage_amount>1
	}

	protected boolean isArmorSlotAvailable() {
		return InventoryHelper.isSlotAvailable(items, MountConf.armorSlot);
	}

	////////////////////////// mc interfaces impls /////////////////////

	public void openInventory(PlayerEntity player) {
		if (conf.isDisabled() || isClient(this)) {
			return;
		}
		if ((!this.hasPassengers() || this.hasPassenger(player))) {
			/* OptionalInt optionalInt = */
			player.openHandledScreen(getScreenHandlerFactory());
		}
	}

	@Override
	public boolean canBeSaddled() {
		return conf.isEnabled() && 20 < age && this.isSaddled();
	}

	@Override
	public boolean isSaddled() {
		return conf.isEnabled() && dataTracker.get(IS_SADDLED);
	}

	@Override
	public void saddle(SoundCategory arg0) {
		if (conf.isDisabled()) {
			return;
		}
		items.setStack(MountConf.saddleSlot, new ItemStack(Items.SADDLE));
	}

	@Override
	public SimpleInventory getInventory() {
		return items;
	}

	/////////////////////////////////////////////////////////////
	// fakery stuff
	protected PassiveEntityMountMixins(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}
}
