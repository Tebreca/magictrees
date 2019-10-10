package com.tebreca.magictrees.proxy.common.obj.blocks.tile;

import com.tebreca.magictrees.proxy.common.registries.MTTileEntityTypes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InfusionAltarTileEntity extends TileEntity {

	Capability<IItemHandler> itemHandlerCapability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	ItemStackHandler inventory = new ItemStackHandler() {
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			return 1;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (world.getBlockState(pos).get(BlockStateProperties.ENABLED)) {
				return ItemStack.EMPTY;
			}
			return super.extractItem(slot, amount, simulate);
		}
	};

	BlockPos master;

	public InfusionAltarTileEntity() {
		super(MTTileEntityTypes.INFUSION_ALTAR);
	}

	public BlockPos getMaster() {
		return master;
	}

	public void setMaster(BlockPos master) {
		this.master = master;
	}

	@Override
	public void remove() {
		if(world.isRemote)
			return;
		if(master != null) ((InfusionTableTileEntity) world.getTileEntity(master)).onSlaveDestroyed();
		ItemStack stack = inventory.getStackInSlot(0);
		if(!stack.isEmpty()){
			world.addEntity(new ItemEntity(world, pos.getX() +0.5,pos.getY() +0.5,pos.getZ() +0.5, stack));
		}
		super.remove();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction direction) {
		return cap == itemHandlerCapability ? LazyOptional.of(() -> (T) inventory) : LazyOptional.empty();
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("inventory", itemHandlerCapability.writeNBT(inventory, Direction.DOWN));
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if(!compound.contains("inventory"))
			return;
		itemHandlerCapability.readNBT(inventory, Direction.DOWN, compound.get("inventory"));
	}


}
