package me.teluri.ghorse;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.teluri.ghorse.conf.Config;
import me.teluri.ghorse.conf.data.MountConf;

public class GenericHorseness implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("generic_horseness");

	@Override
	public void onInitialize() {
		confs = Config.readConf();
		confs=confs;
	}

	protected static HashMap<String, MountConf> confs = new HashMap<>();

	protected static MountConf defconf = new MountConf();

	public static MountConf getConfig(Entity ent) {
		return confs.getOrDefault(EntityType.getId(ent.getType()).toString(), defconf);
	}

}