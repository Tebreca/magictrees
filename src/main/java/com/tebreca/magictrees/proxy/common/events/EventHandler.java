package com.tebreca.magictrees.proxy.common.events;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.Main;
import com.tebreca.magictrees.proxy.client.TreeResourceGenerator;
import com.tebreca.magictrees.proxy.client.resourcepack.FolderManager;
import com.tebreca.magictrees.proxy.client.resourcepack.ResourcePack;
import com.tebreca.magictrees.proxy.client.resourcepack.ResourcePackProvider;
import com.tebreca.magictrees.proxy.common.obj.blocks.tile.CrushingTableTileEntity;
import com.tebreca.magictrees.proxy.common.obj.blocks.tile.InfusionTableTileEntity;
import com.tebreca.magictrees.proxy.common.obj.registry.CrushingTableRecipe;
import com.tebreca.magictrees.proxy.common.obj.registry.InfusionRecipe;
import com.tebreca.magictrees.proxy.common.obj.registry.MineralRegistry;
import com.tebreca.magictrees.proxy.common.obj.registry.Tree;
import com.tebreca.magictrees.proxy.common.registries.MTBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class EventHandler {

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		Main.init();
		event.getRegistry().registerAll(Main.getBlockRegistry().getEntries());
		for (Tree tree : MineralRegistry.getTrees()) {
			tree.streamBlocks().forEach(event.getRegistry()::register);
		}
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		((MTBlocks) Main.getBlockRegistry()).initItemBlocks();
		event.getRegistry().registerAll(Main.getItemRegistry().getEntries());
		event.getRegistry().registerAll(Main.getItemBlockRegistry().getEntries());
		for (Tree tree : MineralRegistry.getTrees()) {
			//TODO: itemgroup
			tree.streamBlocks().map(block -> new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(block.getRegistryName())).forEach(event.getRegistry()::register);
		}
	}


	@SubscribeEvent
	public void registerTETypes(RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().registerAll(Main.getTeTypeRegistry().getEntries());
	}

	@SubscribeEvent
	public void registerCrushingTableRecipes(RegistryEvent.Register<CrushingTableRecipe> event) {
		event.getRegistry().registerAll(MineralRegistry.getCrusherRecipes());
	}

	@SubscribeEvent
	public void addRegistries(RegistryEvent.NewRegistry event) {
		//crushing recipes
		RegistryBuilder<CrushingTableRecipe> builder = new RegistryBuilder<>();
		ResourceLocation location = new ResourceLocation(Constants.MODID, "crushingtablerecipes");
		builder.setType(CrushingTableRecipe.class);
		builder.setName(location);
		IForgeRegistry<CrushingTableRecipe> forgeRegistry = builder.create();
		Mod.EventBusSubscriber.Bus.MOD.bus().get().post(new RegistryEvent.Register<>(location, forgeRegistry));
		CrushingTableTileEntity.recipes = forgeRegistry;
		//infusion recipes
		RegistryBuilder<InfusionRecipe> builder1 = new RegistryBuilder<>();
		ResourceLocation location1 = new ResourceLocation(Constants.MODID, "infusionrecipes");
		builder1.setType(InfusionRecipe.class).setName(location1);
		IForgeRegistry<InfusionRecipe> infusionRecipes = builder1.create();
		Mod.EventBusSubscriber.Bus.MOD.bus().get().post(new RegistryEvent.Register<>(location1, infusionRecipes));
		InfusionTableTileEntity.infusionRecipes = infusionRecipes;
	}

	@SubscribeEvent
	public void registerInfusionRecipes(RegistryEvent.Register<InfusionRecipe> event) {
		event.getRegistry().registerAll(Main.getInfusionRecipeRegistry().getEntries());
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		if(!FolderManager.setup()){
			System.out.println("Failed to setup custom resourcepack for magictrees!!");
			return;
		}
		MineralRegistry.getMinerals().forEach(TreeResourceGenerator::forMineral);
		DistExecutor.runWhenOn(Dist.CLIENT,()-> ()-> Minecraft.getInstance().getResourcePackList().addPackFinder(new ResourcePackProvider()));
	}


}
