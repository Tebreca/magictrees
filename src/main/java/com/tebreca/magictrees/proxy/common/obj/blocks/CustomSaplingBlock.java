package com.tebreca.magictrees.proxy.common.obj.blocks;

import net.minecraft.block.*;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class CustomSaplingBlock extends Block implements IGrowable {

	private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private final GrowFunc growFunc;
	private IProperty<Integer> AGE_0_7;

	public CustomSaplingBlock(GrowFunc growFunc) {
		super(Properties.from(Blocks.OAK_SAPLING).tickRandomly());
		this.growFunc = growFunc;
	}

	private void initProperties() {
		this.AGE_0_7 = BlockStateProperties.AGE_0_7;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		initProperties();
		builder.add(AGE_0_7);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return ((World)worldIn).canBlockSeeSky(pos)&& !isClient;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
		boolean flag = rand.nextInt(10) > 3;
		if(flag && canGrow(worldIn, pos, state, worldIn.isRemote)){
			int stage = state.get(AGE_0_7);
			stage++;
			if(stage >= 7){
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
				this.growFunc.grow(worldIn, pos);
			} else {
				worldIn.setBlockState(pos, state.with(AGE_0_7, stage));
			}
		}
	}

	public interface GrowFunc {
		void grow(World worldIn, BlockPos pos);
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		grow(worldIn, RANDOM, pos, state);
	}

	@Override
	public int tickRate(IWorldReader worldIn) {
		return 80;
	}
}
