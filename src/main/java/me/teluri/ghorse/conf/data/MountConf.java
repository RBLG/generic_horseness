package me.teluri.ghorse.conf.data;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

import me.teluri.ghorse.mount.IControlType;
import me.teluri.ghorse.mount.NoControls;
import me.teluri.ghorse.util.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class MountConf {

	@SerializedName(MountConf.ENABLED)
	private final boolean enabled = false;
	@SerializedName(MountConf.RIDEABLE)
	public final boolean riddeable = false;
	@SerializedName(MountConf.CONTROL_TYPE)
	public final IControlType controls = new NoControls();
	@SerializedName(MountConf.ARMORS)
	private final HashMap<String, ArmorConf> armors__ = new HashMap<>();
	@SerializedName(MountConf.SADDLES)
	private final HashMap<String, SaddleConf> saddles_ = new HashMap<>();
	@SerializedName(MountConf.STORAGES)
	private final HashMap<String, StorageConf> storages = new HashMap<>();
	public static final String CONTROL_TYPE = "controlType";
	public static final String STORAGES = "storages";
	public static final String SADDLES = "saddles";
	public static final String ARMORS = "armors";
	public static final String RIDEABLE = "rideable";
	public static final String ENABLED = "enabled";
	// public final @Nullable Object chestRenderingOffsetHint; // TODO rendering
	public static final String IDENTIFIER = "entity";

	// duplicated info but allow avoiding nulls for armors, saddles and storages
	// public final boolean saddleable;
	// public final boolean armorable;
	// public final boolean storageable;

	// probably never changing, but who knows
	public static final int saddleSlot = 0;
	public static final int armorSlot = 1;
	public static final int storageSlot = 2;

	/*
	 * public MountConf() { this(false, false, null, null, null, null, null); }
	 * 
	 * public MountConf(// boolean nenabled, // boolean nriddeable, //
	 * 
	 * @Nullable HashMap<String, ArmorConf> narmors, //
	 * 
	 * @Nullable HashMap<String, SaddleConf> nsaddles, //
	 * 
	 * @Nullable HashMap<String, StorageConf> nstorages, //
	 * 
	 * @NotNull IControlType ncontrols, //
	 * 
	 * @Nullable Object nchestRenderingOffsetHint// ) { this.enabled = nenabled;
	 * this.riddeable = nriddeable; this.controls = ncontrols;
	 * this.chestRenderingOffsetHint = nchestRenderingOffsetHint; this.armors =
	 * narmors; this.saddles = nsaddles; this.storages = nstorages; this.armorable =
	 * narmors.size() != 0 && !hasOnlyNoneOption(narmors); this.saddleable =
	 * nsaddles.size() != 0 && !hasOnlyNoneOption(nsaddles); this.storageable =
	 * nstorages.size() != 0 && !hasOnlyNoneOption(nstorages); }
	 */

	public static final String EMPTY_SLOT_ID = "minecraft:air";

	public boolean isDisabled() { // for the sake of readability (yes i think it help)
		return !enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isFittingArmor(ItemStack stack) {
		return armors__.containsKey(ItemStackHelper.getIdString(stack));
	}

	public boolean isFittingSaddle(ItemStack stack) {
		return saddles_.containsKey(ItemStackHelper.getIdString(stack));
	}

	public boolean isFittingStorage(ItemStack stack) {
		return storages.containsKey(ItemStackHelper.getIdString(stack));
	}

	public ArmorConf getArmorConf(ItemStack stack) {
		return armors__.get(ItemStackHelper.getIdString(stack));
	}

	public SaddleConf getSaddleConf(ItemStack stack) {
		return saddles_.get(ItemStackHelper.getIdString(stack));
	}

	public StorageConf getStorageConf(ItemStack stack) {
		return storages.get(ItemStackHelper.getIdString(stack));
	}

}
