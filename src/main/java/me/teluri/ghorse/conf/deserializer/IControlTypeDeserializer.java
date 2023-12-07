package me.teluri.ghorse.conf.deserializer;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import me.teluri.ghorse.mount.IControlType;
import me.teluri.ghorse.mount.NoControls;

public class IControlTypeDeserializer implements JsonDeserializer<IControlType> {

	@Override
	public IControlType deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO control type deserialization
		return new NoControls();
	}

}
