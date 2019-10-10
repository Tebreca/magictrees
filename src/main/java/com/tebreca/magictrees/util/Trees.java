package com.tebreca.magictrees.util;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.common.obj.blocks.CustomLeavesBlock;
import com.tebreca.magictrees.proxy.common.obj.blocks.CustomSaplingBlock;
import com.tebreca.magictrees.proxy.common.obj.blocks.InfusedLogBlock;
import com.tebreca.magictrees.proxy.common.obj.registry.Mineral;
import com.tebreca.magictrees.proxy.common.obj.registry.Tree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class Trees {

	private static final Random RANDOM = new Random();

	private Trees() {
	}

	public static Tree forMineral(Mineral mineral) {
		if(mineral.getTree() != null){
			return mineral.getTree();
		}
		InfusedLogBlock log = (InfusedLogBlock) new InfusedLogBlock(mineral).setRegistryName(new ResourceLocation(Constants.MODID, "infused_log_" + mineral.getRegistryName().getPath()));
		Block leaves = new CustomLeavesBlock(log).setRegistryName(new ResourceLocation(Constants.MODID, "infused_leaves_" + mineral.getRegistryName().getPath()));;
		Block sapling = new CustomSaplingBlock((world, pos) -> {
			int height = 5 + RANDOM.nextInt(2);
			for (int y = 0; y < height; y++) {
				BlockPos yPos = pos.up(y);
				world.setBlockState(yPos, generateLog(log));
				if(y < height-1 && y > height-4){
					for (int x =-2; x <3; x++){
						for (int z =-2; z <3; z++){
							if(x==0&&z==0){
								continue;
							}
							world.setBlockState(yPos.add(x, 0, z), leaves.getDefaultState());
						}
					}
				}
			}
			BlockPos top = pos.up(height-1);
			world.setBlockState(top.north(), leaves.getDefaultState());
			world.setBlockState(top.east(), leaves.getDefaultState());
			world.setBlockState(top.south(), leaves.getDefaultState());
			world.setBlockState(top.west(), leaves.getDefaultState());
			world.setBlockState(top, leaves.getDefaultState());
		}).setRegistryName(new ResourceLocation(Constants.MODID, "infused_sapling_" + mineral.getRegistryName().getPath()));
		mineral.setTree(new Tree(log, leaves, sapling, Tree.DefaultTreeTypes.METALLIC));
		return mineral.getTree();
	}

	private static BlockState generateLog(InfusedLogBlock log) {
		BlockState state = log.getDefaultState();
		for (IProperty iProperty : log.FACE_INFUSED) {
			state = state.with(iProperty, RANDOM.nextInt(3) == 3);
		}
		return state;
	}
}
