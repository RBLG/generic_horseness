package me.teluri.ghorse.conf.deserializer;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import me.teluri.ghorse.GenericHorseness;
import me.teluri.ghorse.conf.data.SaddleConf;

public class SaddleConfHashMapDeserializer implements JsonDeserializer<HashMap<String, SaddleConf>> {

	private Gson gson = new Gson();

	@Override
	public HashMap<String, SaddleConf> deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonArray array = arg0.getAsJsonArray();
		HashMap<String, SaddleConf> confs = new HashMap<>();
		for (JsonElement jeconf : array) {
			JsonObject joconf = jeconf.getAsJsonObject();
			JsonElement jekey = joconf.get(SaddleConf.IDENTIFIER);
			if (jekey == null) {
				GenericHorseness.LOGGER.warn("missing item id field in saddle configuration");
				continue;
			}
			String key = jekey.getAsString();
			SaddleConf conf = gson.fromJson(joconf, SaddleConf.class);
			if (confs.get(key) != null) {
				GenericHorseness.LOGGER.warn("saddle configuration ignored because of duplicated item id");
				continue;
			}
			confs.put(key, conf);
		}
		return confs;
	}

}
