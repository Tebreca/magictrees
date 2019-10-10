package com.tebreca.magictrees.proxy.common.obj.blocks;


import com.tebreca.magictrees.proxy.common.obj.registry.Mineral;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class InfusedLogBlock extends Block {

	private final Mineral mineral;
	public IProperty[] FACE_INFUSED;

	public InfusedLogBlock(Mineral mineral) {
		super(Properties.from(Blocks.OAK_LOG));
		BlockState baseState = this.getStateContainer().getBaseState();
		for (IProperty property : FACE_INFUSED) {
			baseState = baseState.with(property, Boolean.FALSE);
		}
		setDefaultState(baseState);
		this.mineral = mineral;
	}

	private void initProperties() {
		this.FACE_INFUSED = new IProperty[]{
				BooleanProperty.create("north_infused"),
				BooleanProperty.create("east_infused"),
				BooleanProperty.create("south_infused"),
				BooleanProperty.create("west_infused")
		};
	}

	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.AXE;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if(worldIn.isRemote()){
			super.onBlockHarvested(worldIn, pos, state, player);
			return;
		}
		ItemStack stack = new ItemStack(this.asItem());
		int count =0;
		for (IProperty iProperty : FACE_INFUSED) {
			if(state.get((IProperty<Boolean>) iProperty)){
				count++;
			}
		}
		if(!stack.hasTag()){
			stack.setTag(new CompoundNBT());
			stack.getTag().putInt("infusion_value", count);
		}
		worldIn.addEntity(new ItemEntity(worldIn, pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5, stack));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		initProperties();
		for (IProperty<?> property : FACE_INFUSED) {
			builder.add(property);
		}
	}
}
