package com.tebreca.magictrees.proxy.common.obj.blocks.tile;

import com.tebreca.magictrees.proxy.common.obj.registry.InfusionRecipe;
import com.tebreca.magictrees.proxy.common.registries.MTTileEntityTypes;
import com.tebreca.magictrees.util.Collections;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InfusionTableTileEntity extends TileEntity implements ITickableTileEntity {

	public static IForgeRegistry<InfusionRecipe> infusionRecipes;

	Capability<IItemHandler> itemHandlerCapability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	private Direction[] horizontal_facings = {Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
	private BlockPos[] slaves = new BlockPos[4];
	private int progress;
	private InfusionRecipe currentRecipe = null;
	private int tick = 0;
	ItemStackHandler inventory = new ItemStackHandler() {
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			return 1;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (isProcessing() || tick != 1) {
				return ItemStack.EMPTY;
			}
			return super.extractItem(slot, amount, simulate);
		}
	};

	public InfusionTableTileEntity() {
		super(MTTileEntityTypes.INFUSION_TABLE);
	}

	private List<Item> slaveItems() {
		return Arrays.stream(slaves).map(world::getTileEntity).map(tileEntity -> (InfusionAltarTileEntity) tileEntity)//
				.map(infusionAltarTileEntity -> infusionAltarTileEntity.inventory)//
				.map(iItemHandler -> iItemHandler.getStackInSlot(0)).map(ItemStack::getItem)//
				.collect(Collectors.toList());//
	}

	public void onSlaveDestroyed() {
		if (!checkSlaves()) {
			cancelRecipe();
		}
	}

	private void cancelRecipe() {
		System.out.println("Cancelling recipe!!");
		currentRecipe = null;
		progress = 0;
	}

	@Override
	public void tick() {
		if (world.isRemote)
			return;
		if (++tick >= 10) {
			tick = 0;
			if (checkSlaves()) {
				if (!isProcessing() && !canProcess()) {
					return;
				} else if (!checkItems()) {
					cancelRecipe();
					return;
				}
				addProgress(1);
				if (isDone()) {
					finishRecipe();
				}
			}
		}
	}

	private boolean checkItems() {
		return currentRecipe.getBaseItem() == inventory.getStackInSlot(0).getItem() && Collections.compare(Arrays.asList(currentRecipe.getInfusers()), slaveItems());
	}

	private void finishRecipe() {
		progress = 0;
		inventory.setStackInSlot(0, new ItemStack(currentRecipe.getOutput()));
		for (BlockPos slave : slaves) {
			((InfusionAltarTileEntity) world.getTileEntity(slave)).inventory.setStackInSlot(0, ItemStack.EMPTY);
		}
		currentRecipe = null;
		for (BlockPos slave : slaves) {
			world.setBlockState(slave, world.getBlockState(slave).with(BlockStateProperties.ENABLED, Boolean.FALSE));
		}
	}

	private boolean isDone() {
		//  TODO: animation length
		int required = 20;
		return progress == required;
	}

	private void addProgress(int i) {
		markDirty();
		progress += i;
	}

	private boolean canProcess() {
		InfusionRecipe recipe = null;
		for (InfusionRecipe recipe1 : infusionRecipes) {
			if (recipe1.getBaseItem() == inventory.getStackInSlot(0).getItem() && Collections.compare(Arrays.asList(recipe1.getInfusers()), slaveItems())) {
				recipe = recipe1;
				break;
			}
		}
		if (recipe == null)
			return false;
		currentRecipe = recipe;
		for (BlockPos slave : slaves) {
			CompoundNBT compoundNBT = world.getTileEntity(slave).write(new CompoundNBT());
			world.setBlockState(slave, world.getBlockState(slave).with(BlockStateProperties.ENABLED, Boolean.TRUE));
			world.getTileEntity(slave).read(compoundNBT);

		}
		return true;
	}

	public boolean isProcessing() {
		return currentRecipe != null;
	}

	private boolean checkSlaves() {
		for (int index = 0; index < slaves.length; index++) {
			BlockPos value = slaves[index];
			if (value == null) {
				boolean foundSlave = tryFindSlave(horizontal_facings[index], index);
				if (!foundSlave) return false;
				continue;
			}
			if (world.getTileEntity(value) instanceof InfusionAltarTileEntity) {
				continue;
			}

			slaves[index] = null;
			return false;
		}
		return true;
	}

	private boolean tryFindSlave(Direction facing, int index) {
		BlockPos newPos = pos.offset(facing, 2);
		if (world.getTileEntity(newPos) instanceof InfusionAltarTileEntity) {
			slaves[index] = newPos;
			return true;
		}
		return false;
	}


	public BlockPos[] getSlaves() {
		return slaves;
	}

	public int getProgress() {
		return progress;
	}

	public Object getCurrentRecipe() {
		return currentRecipe;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("inventory", itemHandlerCapability.writeNBT(inventory, null));
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (!compound.contains("inventory"))
			return;
		itemHandlerCapability.readNBT(inventory, null, compound.get("inventory"));
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction direction) {
		return cap == itemHandlerCapability ? LazyOptional.of(() -> (T) inventory) : LazyOptional.empty();
	}
}
