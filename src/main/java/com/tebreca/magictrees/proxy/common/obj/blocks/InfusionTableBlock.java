package com.tebreca.magictrees.proxy.common.obj.blocks;

import com.tebreca.magictrees.proxy.common.obj.blocks.tile.InfusionTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class InfusionTableBlock extends Block {

	public InfusionTableBlock() {
		super(Properties.from(Blocks.END_STONE));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new InfusionTableTileEntity();
	}
}
