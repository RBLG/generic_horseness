package me.teluri.ghorse.conf.data;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class ConfigRoot {
	public static final String CONFIGS = "mounts";

	@SerializedName(ConfigRoot.CONFIGS)
	public HashMap<String, MountConf> mounts;
}
