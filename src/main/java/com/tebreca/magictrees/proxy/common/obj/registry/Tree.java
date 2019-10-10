package com.tebreca.magictrees.proxy.common.obj.registry;

import com.tebreca.magictrees.Constants;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.stream.Stream;

public class Tree {

	private final Block log;
	private final Block leave;
	private final Block sapling;
	private final TreeType treeType;

	public Tree(Block log, Block leave, Block sapling, TreeType treeType) {
		this.log = log;
		this.leave = leave;
		this.sapling = sapling;
		this.treeType = treeType;
	}

	public Block getLog() {
		return log;
	}

	public Block getLeave() {
		return leave;
	}

	public Block getSapling() {
		return sapling;
	}

	public TreeType getTreeType() {
		return treeType;
	}

	public Stream<Block> streamBlocks() {
		return Arrays.stream(new Block[]{log, leave, sapling});
	}

	public static class DefaultTreeTypes {

		public static final TreeType METALLIC = new TreeType(new ResourceLocation(Constants.MODID, "metallic"));
		public static final TreeType UTIL = new TreeType(new ResourceLocation(Constants.MODID, "util"));
		private DefaultTreeTypes() {
		}

	}

	public static class TreeType {

		private final ResourceLocation name;

		public TreeType(ResourceLocation name) {
			this.name = name;
		}

		public ResourceLocation getName() {
			return name;
		}
	}

}
