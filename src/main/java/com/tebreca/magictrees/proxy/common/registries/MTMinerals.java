package com.tebreca.magictrees.proxy.common.registries;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.common.obj.registry.Mineral;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class MTMinerals implements IRegistry<Mineral> {

	public static final Mineral IRON = new Mineral(MTItems.IRON_DUST, Blocks.IRON_BLOCK.asItem(), Blocks.IRON_ORE.asItem(), Items.IRON_INGOT, 0xfff2fd).setRegistryName(new ResourceLocation(Constants.MODID, "iron"));
	public static final Mineral DIAMOND = new Mineral(MTItems.DIAMOND_DUST, Blocks.DIAMOND_BLOCK.asItem(), Blocks.DIAMOND_ORE.asItem(), Items.DIAMOND, 0x33ddfb).setRegistryName(new ResourceLocation(Constants.MODID, "diamond"));
	public static final Mineral GOLD = new Mineral(MTItems.GOLD_DUST, Blocks.GOLD_BLOCK.asItem(), Blocks.GOLD_ORE.asItem(), Items.GOLD_INGOT, 0xfbed3c).setRegistryName(new ResourceLocation(Constants.MODID, "gold"));

	@Override
	public Mineral[] getEntries() {
		return new Mineral[]{IRON, DIAMOND, GOLD};
	}

}
