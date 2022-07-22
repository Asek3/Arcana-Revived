package net.arcanamod.blocks.tiles;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemStackHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PedestalTileEntity extends TileEntity{
	
	public PedestalTileEntity(){
		super(ArcanaTiles.PEDESTAL_TE.get());
	}
	
	protected ItemStackHandler items = new ItemStackHandler(1){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};
	
	public ItemStack getItem(){
		return items.getStackInSlot(0);
	}
	
	public void setItem(ItemStack stack){
		items.setStackInSlot(0, stack);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
		if(compound.contains("items"))
			items.deserializeNBT(compound.getCompound("items"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		compound.put("items", items.serializeNBT());
		return compound;
	}
	
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	public AxisAlignedBB getRenderBoundingBox(){
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}