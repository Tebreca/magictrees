package com.tebreca.magictrees.proxy.common.obj.blocks;

import com.tebreca.magictrees.proxy.common.registries.MTBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public class TeleportationLeavesBlock extends CustomLeavesBlock {

	public IProperty<Boolean> grown;

	public TeleportationLeavesBlock(Block teleportationLog) {
		super(teleportationLog);
		setDefaultState(getDefaultState().with(grown, false));
	}

	private static boolean touchesAir(World worldIn, BlockPos pos) {
		return Arrays.stream(Direction.values()).map(pos::offset).map(worldIn::isAirBlock).reduce(true, (b, b2) -> b || b2);
	}

	private void initProperties() {
		this.grown = BooleanProperty.create("hasfruit");
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		initProperties();
		builder.add(grown);
		super.fillStateContainer(builder);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {

		final boolean b = touchesAir(worldIn, pos);
		System.out.println(b);
		if (b && !worldIn.isRemote) {
			worldIn.setBlockState(pos, state.with(grown, true));
		}
		super.tick(state, worldIn, pos, random);
	}

	@Override
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
		tick(state, worldIn, pos, random);
		super.randomTick(state, worldIn, pos, random);
	}

	@Override
	public int tickRate(IWorldReader worldIn) {
		return 20;
	}

	@Override
	public Item getSapling() {
		return MTBlocks.TELEPORTATION_SAPLING.asItem();
	}

	@Override
	float getDropChance() {
		return 2f;
	}
}
