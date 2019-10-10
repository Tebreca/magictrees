package com.tebreca.magictrees;

import com.tebreca.magictrees.proxy.client.resourcepack.FolderManager;
import com.tebreca.magictrees.proxy.common.events.EventHandler;
import com.tebreca.magictrees.proxy.common.obj.registry.InfusionRecipe;
import com.tebreca.magictrees.proxy.common.obj.registry.Mineral;
import com.tebreca.magictrees.proxy.common.registries.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Constants.MODID)
@Mod(Constants.MODID)
public class Main {

	private static IRegistry<Block> blockRegistry = new MTBlocks();
	private static IRegistry<Item> itemRegistry = new MTItems();
	private static IRegistry<Mineral> mineralRegistry = new MTMinerals();
	private static IRegistry<TileEntityType> teTypeRegistry = new MTTileEntityTypes();
	private static IRegistry<InfusionRecipe> infusionRecipeRegistry = new MTInfusionRecipes();

	public Main() throws IOException {
		Mod.EventBusSubscriber.Bus.MOD.bus().get().register(new EventHandler());
	}

	public static void init() {

	}

	public static IRegistry<Block> getBlockRegistry() {
		return blockRegistry;
	}

	public static IRegistry<InfusionRecipe> getInfusionRecipeRegistry() {
		return infusionRecipeRegistry;
	}

	public static IRegistry<Item> getItemRegistry() {
		return itemRegistry;
	}

	public static IRegistry<Mineral> getMineralRegistry() {
		return mineralRegistry;
	}


	public static IRegistry<Item> getItemBlockRegistry() {
		return ((MTBlocks) blockRegistry)::getItemBlocks;
	}


	public static IRegistry<TileEntityType> getTeTypeRegistry() {
		return teTypeRegistry;
	}
}
