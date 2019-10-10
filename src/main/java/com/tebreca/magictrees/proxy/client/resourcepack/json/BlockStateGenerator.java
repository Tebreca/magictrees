package com.tebreca.magictrees.proxy.client.resourcepack.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public class BlockStateGenerator {

	private static Gson gson = new Gson();

	private BlockStateGenerator() {
	}

	public static JsonObject generateLogBlockState(ResourceLocation side_normal, ResourceLocation side_infused, ResourceLocation down, ResourceLocation up) {
		String format = "{\"forge_marker\":1,\"defaults\":{\"model\":\"cube_all\",\"textures\":{\"down\":\"%2$s\",\"up\":\"%3$s\",\"north\":\"%0$s\",\"east\":\"%0$s\",\"west\":\"%0$s\",\"south\":\"%0$s\",\"particle\":\"%1$s\"}},\"variants\":{\"normal\":[{}],\"inventory\":[{}],\"north_infused\":{\"true\":{\"textures\":{\"north\":\"%1$s\"}},\"false\":{}},\"east_infused\":{\"true\":{\"textures\":{\"east\":\"%1$s\"}},\"false\":{}},\"south_infused\":{\"true\":{\"textures\":{\"south\":\"%1$s\"}},\"false\":{}},\"west_infused\":{\"true\":{\"textures\":{\"west\":\"%1$s\"}},\"false\":{}}}}";
		String object = String.format(format, side_normal, side_infused, down, up);
		return gson.fromJson(object, JsonObject.class);
	}

	public static JsonObject generateCubeBlockState(ResourceLocation up, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west, ResourceLocation down, ResourceLocation particle) {
		String format = "{\"forge_marker\":1,\"defaults\":{\"model\":\"cube_all\",\"textures\":{\"down\":\"%s\",\"up\":\"%s\",\"north\":\"%s\",\"east\":\"%s\",\"west\":\"%s\",\"south\":\"%s\",\"particle\":\"%s\"}},\"variants\":{\"normal\":[{}],\"inventory\":[{}]}}";
		String object = String.format(format, down, up, north, east, west, south, particle);
		return gson.fromJson(object, JsonObject.class);
	}

	public static JsonObject generateCubeBlockState(ResourceLocation all) {
		String format = "{\"forge_marker\":1,\"defaults\":{\"model\":\"cube_all\",\"textures\":{\"all\":\"%s\"}},\"variants\":{\"normal\":[{}],\"inventory\":[{}]}}";
		String object = String.format(format, all);
		return gson.fromJson(object, JsonObject.class);
	}

}
