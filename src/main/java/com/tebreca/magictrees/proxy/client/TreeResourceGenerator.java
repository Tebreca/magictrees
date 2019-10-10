package com.tebreca.magictrees.proxy.client;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.client.resourcepack.FolderManager;
import com.tebreca.magictrees.proxy.client.resourcepack.img.ImageGenerator;
import com.tebreca.magictrees.proxy.client.resourcepack.json.BlockStateGenerator;
import com.tebreca.magictrees.proxy.common.obj.registry.Mineral;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;


public class TreeResourceGenerator {

	private static final Map<Mineral, MineralTextureHolder> cached = new HashMap<>();
	private static final Gson gson = new GsonBuilder().create();

	private TreeResourceGenerator() {

	}

	public static Map<Mineral, MineralTextureHolder> getCachedTextureHolders() {
		return ImmutableMap.copyOf(cached);
	}

	public static void forMineral(Mineral mineral) {
		ResourceLocation logTopTexture = generateLogTopTexture(mineral);
		ResourceLocation logSideTexture = generateLogSideTexture(mineral);
		ResourceLocation logBlockState = generateLogBlockState(mineral, logTopTexture, logSideTexture);
		ResourceLocation leavesTexture = generateLeavesTexture(mineral);
		ResourceLocation leaveBlockState = generateLeaveBlockState(mineral, leavesTexture);
		MineralTextureHolder mineralTextureHolder = new MineralTextureHolder(logSideTexture, logTopTexture, logBlockState, leavesTexture, leaveBlockState, null, null);
		cached.put(mineral, mineralTextureHolder);
	}

	private static ResourceLocation generateLogTopTexture(Mineral mineral) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, "texture_d_log_top_" + mineral.getRegistryName().getPath());
		try {
			File output = FolderManager.forResource(location, "png");
			File base = new File(FolderManager.getStaticFolder(), "ore_log_top.png");
			File overlay = new File(FolderManager.getStaticFolder(), "log_overlay.png");
			Image texture = ImageGenerator.overlay(new ImageIcon(base.getAbsolutePath()).getImage(), new ImageIcon(overlay.getAbsolutePath()).getImage(), new Color(mineral.getColour()));
			assert output != null;
			ImageIO.write((RenderedImage) texture, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Failed to generate top log texture for mineral %s", mineral.getRegistryName().toString());
		}
		return location;
	}

	private static ResourceLocation generateLogSideTexture(Mineral mineral) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, "texture_d_log_side_" + mineral.getRegistryName().getPath());
		try {
			File output = FolderManager.forResource(location, "png");
			File base = new File(FolderManager.getStaticFolder(), "ore_log_side_resin.png");
			File overlay = new File(FolderManager.getStaticFolder(), "resin_overlay.png");
			Image texture = ImageGenerator.overlay(new ImageIcon(base.getAbsolutePath()).getImage(), new ImageIcon(overlay.getAbsolutePath()).getImage(), new Color(mineral.getColour()));
			assert output != null;
			ImageIO.write((RenderedImage) texture, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Failed to generate side log texture for mineral %s", mineral.getRegistryName().toString());
		}
		return location;
	}

	private static ResourceLocation generateLogBlockState(Mineral mineral, ResourceLocation topTexure, ResourceLocation sideTexture) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, "infused_log_" + mineral.getRegistryName().getPath());
		try {
			File output = FolderManager.forResource(location, "json");
			JsonObject blockstate = BlockStateGenerator.generateLogBlockState(MineralTextureHolder.getLogSideEmptyTexture(), sideTexture, topTexure, topTexure);
			assert output != null;
			JsonWriter writer = new JsonWriter(new FileWriter(output));
			gson.toJson(blockstate, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Failed to generate log blockstate for mineral %s", mineral.getRegistryName().toString());
		}
		return location;
	}

	private static ResourceLocation generateLeavesTexture(Mineral mineral) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, "texture_d_leaves_" + mineral.getRegistryName().getPath());
		try {
			File output = FolderManager.forResource(location, "png");
			File base = new File(FolderManager.getStaticFolder(), "leaves_metal.png");
			File overlay = base;
			Image texture = ImageGenerator.overlay(new ImageIcon(base.getAbsolutePath()).getImage(), new ImageIcon(overlay.getAbsolutePath()).getImage(), new Color(mineral.getColour()));
			assert output != null;
			ImageIO.write((RenderedImage) texture, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Failed to generate leave texture for mineral %s", mineral.getRegistryName().toString());
		}
		return location;
	}

	private static ResourceLocation generateLeaveBlockState(Mineral mineral, ResourceLocation leaveTexture) {
		ResourceLocation location = new ResourceLocation(Constants.MODID, "infused_leaves_" + mineral.getRegistryName().getPath());
		try {
			File output = FolderManager.forResource(location, "json");
			JsonObject blockstate = BlockStateGenerator.generateCubeBlockState(leaveTexture);
			assert output != null;
			JsonWriter writer = new JsonWriter(new FileWriter(output));
			gson.toJson(blockstate, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Failed to generate leave blockstate for mineral %s", mineral.getRegistryName().toString());
		}
		return location;
	}

}
