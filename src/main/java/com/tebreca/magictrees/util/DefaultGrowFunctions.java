package com.tebreca.magictrees.util;

import com.tebreca.magictrees.proxy.common.obj.blocks.CustomSaplingBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class DefaultGrowFunctions {

	private static Random random = new Random();

	public static CustomSaplingBlock.GrowFunc of(Block log, Block leaves) {
		return (world, pos) -> {
			int height = 5 + random.nextInt(2);
			for (int y = 0; y < height; y++) {
				BlockPos yPos = pos.up(y);
				world.setBlockState(yPos, log.getDefaultState());
				if (y < height - 1 && y > height - 4) {
					for (int x = -2; x < 3; x++) {
						for (int z = -2; z < 3; z++) {
							if (x == 0 && z == 0) {
								continue;
							}
							world.setBlockState(yPos.add(x, 0, z), leaves.getDefaultState());
						}
					}
				}
			}
			BlockPos top = pos.up(height - 1);
			world.setBlockState(top.north(), leaves.getDefaultState());
			world.setBlockState(top.east(), leaves.getDefaultState());
			world.setBlockState(top.south(), leaves.getDefaultState());
			world.setBlockState(top.west(), leaves.getDefaultState());
			world.setBlockState(top, leaves.getDefaultState());
		};
	}
}
