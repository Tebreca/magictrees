package com.tebreca.magictrees.proxy.common.registries;

import com.tebreca.magictrees.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class MTItems implements IRegistry<Item> {

	public static final Item IRON_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(Constants.MODID, "iron_dust"));
	public static final Item DIAMOND_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(Constants.MODID, "diamond_dust"));
	public static final Item GOLD_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(new ResourceLocation(Constants.MODID, "gold_dust"));

	@Override
	public Item[] getEntries() {
		return new Item[]{IRON_DUST, DIAMOND_DUST, GOLD_DUST};
	}

}
