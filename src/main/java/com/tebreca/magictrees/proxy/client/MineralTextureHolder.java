package com.tebreca.magictrees.proxy.client;

import com.tebreca.magictrees.Constants;
import net.minecraft.util.ResourceLocation;

public class MineralTextureHolder {

	private static final ResourceLocation log_side_empty_texture = new ResourceLocation(Constants.MODID, "blocks/trees/ore_log_side");
	private final ResourceLocation log_side_texture;
	private final ResourceLocation log_top_texture;
	private final ResourceLocation log_model;
	private final ResourceLocation leave_texture;
	private final ResourceLocation leave_model;
	private final ResourceLocation sapling_texure;
	private final ResourceLocation sapling_model;

	public MineralTextureHolder(ResourceLocation log_side_texture, ResourceLocation log_top_texture, ResourceLocation log_model, ResourceLocation leave_texture, ResourceLocation leave_model, ResourceLocation sapling_texure, ResourceLocation sapling_model) {
		this.log_side_texture = log_side_texture;
		this.log_top_texture = log_top_texture;
		this.log_model = log_model;
		this.leave_texture = leave_texture;
		this.leave_model = leave_model;
		this.sapling_texure = sapling_texure;
		this.sapling_model = sapling_model;
	}

	public ResourceLocation getLogSideTexture() {
		return log_side_texture;
	}

	public ResourceLocation getLogTopTexture() {
		return log_top_texture;
	}

	public ResourceLocation getLogModel() {
		return log_model;
	}

	public ResourceLocation getLeaveTexture() {
		return leave_texture;
	}

	public ResourceLocation getLeaveModel() {
		return leave_model;
	}

	public ResourceLocation getSaplingTexure() {
		return sapling_texure;
	}

	public ResourceLocation getSaplingModel() {
		return sapling_model;
	}

	public static ResourceLocation getLogSideEmptyTexture() {
		return log_side_empty_texture;
	}
}
