package com.tebreca.magictrees.proxy.common.obj.blocks.tile;

import com.tebreca.magictrees.proxy.common.obj.registry.CrushingTableRecipe;
import com.tebreca.magictrees.proxy.common.registries.MTTileEntityTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrushingTableTileEntity extends TileEntity {

	public static IForgeRegistry<CrushingTableRecipe> recipes;

	CrushingTableRecipe currentRecipe;

	Capability<IItemHandler> itemHandlerCapability = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	int process;
	ItemStackHandler output = new ItemStackHandler();
	ItemStackHandler input = new ItemStackHandler() {
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			return 1;
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (isCrushable(stack) && output.insertItem(0, getRecipe(stack.getItem()).getOutput(), true).isEmpty()) {
				return super.insertItem(slot, stack, simulate);
			}
			return stack;
		}
	};

	public CrushingTableTileEntity() {
		super(MTTileEntityTypes.CRUSHING_TABLE);
	}

	@Nullable
	private CrushingTableRecipe getRecipe(Item item) {
		List<CrushingTableRecipe> crushingTableRecipes = recipes.getEntries().stream().map(Map.Entry::getValue).collect(Collectors.toList());
		return crushingTableRecipes.stream().filter(recipe -> recipe.getInput() == item).findAny().orElse(null);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == itemHandlerCapability && side != null) {
			switch (side) {
				case DOWN:
					return LazyOptional.of(() -> (T) output);
				case UP:
					break;
				case NORTH:

				case SOUTH:

				case WEST:

				case EAST:
					return LazyOptional.of(() -> (T) input);
			}
		}
		return LazyOptional.empty();
	}

	boolean isProcessing() {
		return currentRecipe != null;
	}

	private boolean isCrushable(ItemStack stackInSlot) {
		return getRecipe(stackInSlot.getItem()) != null;
	}

	public void onActivated() {
		if (!isProcessing() && !tryProcessing()) {
			return;
		}
		process++;
		if (process >= 30) {
			ItemStack output = currentRecipe.getOutput();
			this.output.insertItem(0, output, false);
			this.process = 0;
			this.input.setStackInSlot(0, ItemStack.EMPTY);
			currentRecipe = null;
		}
	}

	private boolean tryProcessing() {
		if (isCrushable(input.getStackInSlot(0))) {
			this.currentRecipe = getRecipe(input.getStackInSlot(0).getItem());
			this.process = 0;
			return true;
		}

		return false;
	}

	public List<ItemStack> getContents() {
		return Arrays.asList(new ItemStack[]{output.getStackInSlot(0), input.getStackInSlot(0)});
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("input", itemHandlerCapability.writeNBT(input, null));
		compound.put("output", itemHandlerCapability.writeNBT(output, null));
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.contains("input")) {
			itemHandlerCapability.readNBT(input, null, compound.get("input"));
		}
		if (compound.contains("output")) {
			itemHandlerCapability.readNBT(output, null, compound.get("output"));
		}
	}
}
