package com.tebreca.magictrees.proxy.common.obj.blocks;

import com.tebreca.magictrees.proxy.common.obj.blocks.tile.CrushingTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class CrushingTableBlock extends Block {

	public CrushingTableBlock(Properties properties) {
		super(properties);
	}

	public CrushingTableBlock() {
		this(Properties.from(Blocks.END_STONE).variableOpacity());
	}

	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		return face != Direction.UP;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CrushingTableTileEntity();
	}

	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}

	VoxelShape shape = Block.makeCuboidShape(0,0,0,16,15,16);

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return shape;
	}

	@Override
	public void onLanded(IBlockReader worldIn, Entity entityIn) {
		if(entityIn instanceof PlayerEntity){
			if(!entityIn.world.isRemote){
				TileEntity tileEntity = worldIn.getTileEntity(entityIn.getPosition());
				if(tileEntity instanceof CrushingTableTileEntity){
					((CrushingTableTileEntity) tileEntity).onActivated();
				}
			}
		}
		super.onLanded(worldIn, entityIn);
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if(worldIn.isRemote){
			return;
		}
		CrushingTableTileEntity tileEntity = (CrushingTableTileEntity) worldIn.getTileEntity(pos);
		tileEntity.getContents().stream().map(itemStack -> new ItemEntity(worldIn,pos.getX() + 0.5,pos.getY() + 0.5,pos.getZ() + 0.5, itemStack)).forEach(worldIn::addEntity);
		super.onBlockHarvested(worldIn, pos, state, player);
	}
}

