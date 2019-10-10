package com.tebreca.magictrees.proxy.common.obj.blocks;

import com.tebreca.magictrees.proxy.common.obj.blocks.tile.InfusionAltarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class InfusionAltarBlock extends Block {

	IProperty<Boolean> ENABLED;

	public InfusionAltarBlock() {
		super(Properties.from(Blocks.END_STONE));
		this.setDefaultState(this.getDefaultState().with(ENABLED, Boolean.FALSE));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new InfusionAltarTileEntity();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		ENABLED = BlockStateProperties.ENABLED;
		builder.add(ENABLED);
	}

}
