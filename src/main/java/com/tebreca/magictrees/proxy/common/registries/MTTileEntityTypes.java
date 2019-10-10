package com.tebreca.magictrees.proxy.common.registries;

import com.tebreca.magictrees.Constants;
import com.tebreca.magictrees.proxy.common.obj.blocks.tile.CrushingTableTileEntity;
import com.tebreca.magictrees.proxy.common.obj.blocks.tile.InfusionAltarTileEntity;
import com.tebreca.magictrees.proxy.common.obj.blocks.tile.InfusionTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public class MTTileEntityTypes implements IRegistry<TileEntityType> {

	public static final TileEntityType<?> INFUSION_TABLE = TileEntityType.Builder.create(InfusionTableTileEntity::new, MTBlocks.INFUSION_TABLE).build(null).setRegistryName(new ResourceLocation(Constants.MODID, "infusiontable"));;
	public static TileEntityType<?> CRUSHING_TABLE = TileEntityType.Builder.create(CrushingTableTileEntity::new, MTBlocks.CRUSHING_TABLE).build(null).setRegistryName(new ResourceLocation(Constants.MODID, "crushingtable"));
	public static TileEntityType<?> INFUSION_ALTAR = TileEntityType.Builder.create(InfusionAltarTileEntity::new, MTBlocks.INFUSION_ALTAR).build(null).setRegistryName(new ResourceLocation(Constants.MODID, "infusionaltar"));

	@Override
	public TileEntityType[] getEntries() {
		return new TileEntityType[]{CRUSHING_TABLE, INFUSION_ALTAR, INFUSION_TABLE};
	}
}
