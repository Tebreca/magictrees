package com.tebreca.magictrees.proxy.common.obj.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class CrushingTableRecipe implements IForgeRegistryEntry<CrushingTableRecipe> {

	private ResourceLocation resourceLocation;

	private  final Item input;

	private final ItemStack output;

	public CrushingTableRecipe(Item input, ItemStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public CrushingTableRecipe setRegistryName(ResourceLocation name) {
		this.resourceLocation = name;
		return this;
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return this.resourceLocation;
	}

	@Override
	public Class getRegistryType() {
		return CrushingTableRecipe.class;
	}

	public Item getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output.copy();
	}
}
