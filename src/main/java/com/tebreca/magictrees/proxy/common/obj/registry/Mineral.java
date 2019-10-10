package com.tebreca.magictrees.proxy.common.obj.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class Mineral implements IForgeRegistryEntry<Mineral> {

	final Item dust;
	final Item block;
	final Item ore;
	final Item ingot;
	final int colour;
	Tree tree;
	private ResourceLocation registryName;

	public Mineral(Item dust, Item block, Item ore, Item ingot, int colour) {
		this.dust = dust;
		this.block = block;
		this.ore = ore;
		this.ingot = ingot;
		this.colour = colour;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public Item getDust() {
		return dust;
	}

	public Item getBlock() {
		return block;
	}

	public Item getOre() {
		return ore;
	}

	public Item getIngot() {
		return ingot;
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return this.registryName;
	}

	@Override
	public Mineral setRegistryName(ResourceLocation name) {
		this.registryName = name;
		return this;
	}

	@Override
	public Class getRegistryType() {
		return Mineral.class;
	}

	public int getColour() {
		return colour;
	}
}
