package com.tebreca.magictrees.proxy.common.obj.blocks;

import com.tebreca.magictrees.proxy.common.obj.registry.MineralRegistry;
import com.tebreca.magictrees.proxy.common.obj.registry.Tree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IShearable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomLeavesBlock extends Block implements IShearable {

	private static IntegerProperty DISTANCE;
	private static BooleanProperty PERSISTENT;
	private final Block log;

	public CustomLeavesBlock(Block log) {
		super(Properties.from(Blocks.OAK_LEAVES));
		this.setDefaultState(this.stateContainer.getBaseState().with(DISTANCE, 7).with(PERSISTENT, Boolean.FALSE));
		this.log = log;
	}

	private BlockState updateDistance(BlockState blockState, IWorld world, BlockPos pos) {
		int i = 7;

		try (BlockPos.PooledMutableBlockPos mutableBlockPos = BlockPos.PooledMutableBlockPos.retain()) {
			for (Direction direction : Direction.values()) {
				mutableBlockPos.setPos(pos).move(direction);
				i = Math.min(i, getDistance(world.getBlockState(mutableBlockPos)) + 1);
				if (i == 1) {
					break;
				}
			}
		}

		return blockState.with(DISTANCE, i);
	}

	private int getDistance(BlockState neighbor) {
		if (neighbor.getBlock() == log) {
			return 0;
		} else {
			return neighbor.getBlock() instanceof CustomLeavesBlock ? neighbor.get(DISTANCE) : 7;
		}
	}

	public boolean ticksRandomly(BlockState state) {
		return state.get(DISTANCE) == 7 && !state.get(PERSISTENT);
	}

	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7) {
			spawnDrops(state, worldIn, pos);
			worldIn.removeBlock(pos, false);
		}

	}

	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		worldIn.setBlockState(pos, updateDistance(state, worldIn, pos), 3);
	}

	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		int i = getDistance(facingState) + 1;
		if (i != 1 || stateIn.get(DISTANCE) != i) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return stateIn;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (worldIn.isRainingAt(pos.up())) {
			if (rand.nextInt(15) == 1) {
				BlockPos blockpos = pos.down();
				BlockState blockstate = worldIn.getBlockState(blockpos);
				if (!blockstate.isSolid() || !blockstate.func_224755_d(worldIn, blockpos, Direction.UP)) {
					double d0 = (double) ((float) pos.getX() + rand.nextFloat());
					double d1 = (double) pos.getY() - 0.05D;
					double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
					worldIn.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	public boolean isSolid(BlockState state) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return Minecraft.isFancyGraphicsEnabled() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}

	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	private void initProperties() {
		DISTANCE = BlockStateProperties.DISTANCE_1_7;
		PERSISTENT = BlockStateProperties.PERSISTENT;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		initProperties();
		builder.add(DISTANCE, PERSISTENT);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return updateDistance(this.getDefaultState().with(PERSISTENT, Boolean.TRUE), context.getWorld(), context.getPos());
	}

	float getDropChance(){
		return 0.1f;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		int r = RANDOM.nextInt(100);
		if (r > (100f - getDropChance())) {
			return Collections.singletonList(new ItemStack(Arrays.stream(MineralRegistry.getTrees()).filter(tree -> tree.getLeave() == this).map(Tree::getSapling).map(Block::asItem).findAny().orElse(getSapling())));
		}
		return super.getDrops(state, builder);
	}

	Item getSapling() {
		return Blocks.OAK_SAPLING.asItem();
	}
}
