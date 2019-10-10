package com.tebreca.magictrees.proxy.common.registries;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.common.obj.registry.InfusionRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class MTInfusionRecipes implements IRegistry<InfusionRecipe> {

	InfusionRecipe SAPLING = new InfusionRecipe(Items.DEAD_BUSH, new Item[]{Items.WHEAT_SEEDS,Items.WHEAT_SEEDS,Items.WHEAT_SEEDS,Items.WHEAT_SEEDS}, Items.OAK_SAPLING).setRegistryName(new ResourceLocation(Constants.MODID, "ins_live_into_sapling"));
	InfusionRecipe CHORUS_PLANT = new InfusionRecipe(Items.OAK_SAPLING, new Item[]{Items.CHORUS_FRUIT,Items.CHORUS_FRUIT,Items.CHORUS_FRUIT, Items.CHORUS_FRUIT}, Items.CHORUS_FLOWER).setRegistryName(new ResourceLocation(Constants.MODID, "ins_end_into_sapling"));

	@Override
	public InfusionRecipe[] getEntries() {
		return new InfusionRecipe[]{SAPLING};
	}
}
