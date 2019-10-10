package com.tebreca.magictrees.proxy.common.obj.registry;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class InfusionRecipe implements IForgeRegistryEntry<InfusionRecipe> {

	private final Item baseItem;
	private final Item[] infusers;
	private final Item output;
	private ResourceLocation RegistryName;

	public InfusionRecipe(Item baseItem, Item[] infusers, Item output) {
		this.baseItem = baseItem;
		this.infusers = infusers;
		this.output = output;
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return RegistryName;
	}

	@Override
	public InfusionRecipe setRegistryName(ResourceLocation registryName) {
		RegistryName = registryName;
		return this;
	}

	public Item getBaseItem() {
		return baseItem;
	}

	public Item[] getInfusers() {
		return infusers;
	}

	public Item getOutput() {
		return output;
	}

	@Override
	public Class<InfusionRecipe> getRegistryType() {
		return InfusionRecipe.class;
	}
}
