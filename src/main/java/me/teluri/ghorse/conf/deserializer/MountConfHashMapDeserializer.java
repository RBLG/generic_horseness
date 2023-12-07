package me.teluri.ghorse.conf.deserializer;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import me.teluri.ghorse.GenericHorseness;
import me.teluri.ghorse.conf.data.ArmorConf;
import me.teluri.ghorse.conf.data.MountConf;
import me.teluri.ghorse.conf.data.SaddleConf;
import me.teluri.ghorse.conf.data.StorageConf;
import me.teluri.ghorse.mount.IControlType;

public class MountConfHashMapDeserializer implements JsonDeserializer<HashMap<String, MountConf>> {

	private Gson gson = new GsonBuilder()//
			.registerTypeAdapter(new TypeToken<IControlType>() {}.getType(), new IControlTypeDeserializer())//
			.registerTypeAdapter(new TypeToken<HashMap<String, ArmorConf>>() {}.getType(), new ArmorConfHashMapDeserializer())//
			.registerTypeAdapter(new TypeToken<HashMap<String, SaddleConf>>() {}.getType(), new SaddleConfHashMapDeserializer())//
			.registerTypeAdapter(new TypeToken<HashMap<String, StorageConf>>() {}.getType(), new StorageConfHashMapDeserializer())//
			.create();

	@Override
	public HashMap<String, MountConf> deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonArray array = arg0.getAsJsonArray();
		HashMap<String, MountConf> confs = new HashMap<>();
		int iter = -1; // quick iterator to hint at which element of the list failed in the logs
		for (JsonElement jeconf : array) {
			iter++;
			JsonObject joconf = jeconf.getAsJsonObject();
			JsonElement jekey = joconf.get(MountConf.IDENTIFIER);
			if (jekey == null) {
				GenericHorseness.LOGGER.warn(String.format("missing id field in mount configuration %s", iter));
				continue;
			}
			String key = jekey.getAsString();
			MountConf conf = gson.fromJson(joconf, MountConf.class);
			if (confs.get(key) != null) {
				GenericHorseness.LOGGER.warn(String.format("mount configuration %s ignored because of duplicated id", iter));
				continue;
			}
			confs.put(key, conf);
		}
		return confs;
	}

}
