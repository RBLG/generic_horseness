package me.teluri.ghorse.conf;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import me.teluri.ghorse.GenericHorseness;
import me.teluri.ghorse.conf.data.ConfigRoot;
import me.teluri.ghorse.conf.data.MountConf;
import me.teluri.ghorse.conf.deserializer.MountConfHashMapDeserializer;
import net.fabricmc.loader.api.FabricLoader;

public final class Config {

	private Config() {}

	public static HashMap<String, MountConf> readConf() {
		JsonObject conf = readConfFromDisk();
		return readConfFromJsonObject2(conf);
	}

	public static JsonObject readConfFromDisk() {
		Path confdir = FabricLoader.getInstance().getConfigDir();
		Path cfiledir = confdir.resolve("generic_horseness.json");
		File cfile = cfiledir.toFile();
		JsonObject config;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (!cfile.exists()) { // if conf file doesnt exist, create it and use it from here
			config = getDefaultConfig();
			try {
				Files.writeString(cfiledir, gson.toJson(config));
				GenericHorseness.LOGGER.info("created default config file.");
			} catch (IOException e) {
				GenericHorseness.LOGGER.error("couldnt create default config file. " + e.getMessage());
			}
		} else { // if conf file exist, read it
			String cfilecontent;
			try {
				cfilecontent = Files.readString(cfiledir);
				config = gson.fromJson(cfilecontent, JsonObject.class);
				GenericHorseness.LOGGER.info("successfully read config file.");
			} catch (Exception e) {
				config = getDefaultConfig();
				GenericHorseness.LOGGER.error("couldnt read config file. " + e.getMessage());
			}
		}
		return config;
	}

	public static JsonObject getDefaultConfig() {
		JsonObject dconf = new JsonObject();
		JsonArray mounts = new JsonArray();
		JsonObject beeconf = new JsonObject();
		beeconf.addProperty(MountConf.IDENTIFIER, "minecraft:bee");
		beeconf.addProperty(MountConf.ENABLED, true);
		beeconf.addProperty(MountConf.RIDEABLE, true);
		beeconf.add(MountConf.ARMORS, new JsonArray());
		beeconf.add(MountConf.SADDLES, new JsonArray());
		beeconf.add(MountConf.STORAGES, new JsonArray());
		beeconf.addProperty(MountConf.CONTROL_TYPE, "horse");
		mounts.add(beeconf);
		dconf.add("mounts", mounts);
		return dconf;
	}

	private static Type mountConfHashMapType = new TypeToken<HashMap<String, MountConf>>() {}.getType();

	public static HashMap<String, MountConf> readConfFromJsonObject2(JsonElement root) {
		Gson gson = new GsonBuilder().registerTypeAdapter(mountConfHashMapType, new MountConfHashMapDeserializer()).create();
		try {
			return gson.fromJson(root, ConfigRoot.class).mounts;
		} catch (Exception e) {
			GenericHorseness.LOGGER.error("parsing of configuration json failed.");
			GenericHorseness.LOGGER.error(e.getStackTrace().toString());
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
