package com.tebreca.magictrees.proxy.common.obj.registry;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.Main;
import com.tebreca.magictrees.util.Trees;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MineralRegistry {

	private static List<Mineral> minerals = new ArrayList<>();

	static {
		add(Main.getMineralRegistry().getEntries());
	}

	private MineralRegistry() {
	}

	public static void add(Mineral mineral) {
		minerals.add(mineral);
	}

	public static void add(Mineral... minerals) {
		MineralRegistry.minerals.addAll(Arrays.asList(minerals));
	}

	public static List<Mineral> getMinerals() {
		return Collections.unmodifiableList(minerals);
	}

	public static CrushingTableRecipe[] getCrusherRecipes() {
		List<CrushingTableRecipe> crushingTableRecipes = new ArrayList<>();
		getMinerals().stream().map(Transformers.ORE_DUST_TRANSFORMER).forEach(crushingTableRecipes::add);
		getMinerals().stream().map(Transformers.INGOT_DUST_TRANSFORMER).forEach(crushingTableRecipes::add);
		return crushingTableRecipes.toArray(new CrushingTableRecipe[0]);
	}

	public FurnaceRecipe[] getMeltingRecipes() {
		return getMinerals().stream().map(Transformers.DUST_INGOT_TRANSFORMER).collect(Collectors.toList()).toArray(new FurnaceRecipe[]{});
	}


	public static Tree[] getTrees(){
		return getMinerals().stream().map(Transformers.TREE_TRANSFORMER).collect(Collectors.toList()).toArray(new Tree[]{});
	}

	private static class Transformers {

		static final Function<Mineral, CrushingTableRecipe> ORE_DUST_TRANSFORMER = mineral -> new CrushingTableRecipe(mineral.ore, new ItemStack(mineral.dust, 2)).setRegistryName(new ResourceLocation(Constants.MODID, mineral.getRegistryName().getPath() + "_ore_dust"));

		static final Function<Mineral, CrushingTableRecipe> INGOT_DUST_TRANSFORMER = mineral -> new CrushingTableRecipe(mineral.ingot, new ItemStack(mineral.dust, 1)).setRegistryName(new ResourceLocation(Constants.MODID, mineral.getRegistryName().getPath() + "_ingot_dust"));

		static final Function<Mineral, FurnaceRecipe> DUST_INGOT_TRANSFORMER = mineral -> new FurnaceRecipe(new ResourceLocation(Constants.MODID, mineral.getRegistryName().getPath() + "_dust_ingot"), "ingots", Ingredient.fromItems(mineral.dust), new ItemStack(mineral.ingot), 20, 60);

		static final Function<Mineral, Tree> TREE_TRANSFORMER = Trees::forMineral;


	}
}
