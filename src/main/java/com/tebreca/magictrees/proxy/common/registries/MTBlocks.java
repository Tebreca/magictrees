package com.tebreca.magictrees.proxy.common.registries;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.common.obj.blocks.*;
import com.tebreca.magictrees.util.DefaultGrowFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MTBlocks implements IRegistry<Block> {

	public static Block CRUSHING_TABLE = new CrushingTableBlock().setRegistryName(new ResourceLocation(Constants.MODID, "crushing_table"));
	public static Block INFUSION_ALTAR = new InfusionAltarBlock().setRegistryName(new ResourceLocation(Constants.MODID, "infusion_altar"));
	public static Block INFUSION_TABLE = new InfusionTableBlock().setRegistryName(new ResourceLocation(Constants.MODID, "infusion_table"));
	//teleportation tree
	public static Block TELEPORTATION_LOG = new LogBlock(MaterialColor.FOLIAGE, Block.Properties.from(Blocks.OAK_LOG)).setRegistryName(new ResourceLocation(Constants.MODID, "log_tree_teleportation"));
	public static Block TELEPORTATION_LEAVES = new TeleportationLeavesBlock(TELEPORTATION_LOG).setRegistryName(new ResourceLocation(Constants.MODID, "leaves_tree_teleportation"));
	public static Block TELEPORTATION_SAPLING = new CustomSaplingBlock(DefaultGrowFunctions.of(TELEPORTATION_LOG, TELEPORTATION_LEAVES)).setRegistryName(new ResourceLocation(Constants.MODID, "sapling_tree_teleportation"));


	private Item[] itemBlocks;

	public void initItemBlocks() {
		List<Item> itemBlocks = new ArrayList<>();
		Arrays.stream(this.getEntries()).map(b -> new BlockItem(b, new Item.Properties().group(ItemGroup.REDSTONE)).setRegistryName(Objects.requireNonNull(b.getRegistryName()))).forEach(itemBlocks::add);
		this.itemBlocks = itemBlocks.toArray(new Item[0]);
	}

	public Item[] getItemBlocks() {
		return itemBlocks;
	}

	@Override
	public Block[] getEntries() {
		return new Block[]{CRUSHING_TABLE, INFUSION_ALTAR, INFUSION_TABLE, TELEPORTATION_LEAVES, TELEPORTATION_LOG, TELEPORTATION_SAPLING};
	}

}
