package net.arcanamod.blocks.pipes;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.arcanamod.containers.PumpContainer;
import net.arcanamod.items.CrystalItem;
import net.arcanamod.items.EnchantedFilterItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PumpTileEntity extends TubeTileEntity implements INamedContainerProvider{
	
	protected static int PULL_AMOUNT = 4;
	protected static int PULL_TIME = 5;
	protected static float SPECK_SPEED = 0.5f;
	
	public boolean suppressedByRedstone = false;
	public ItemStackHandler inventory = new ItemStackHandler(2); // filter, crystal
	public Direction direction;
	
	// pull aspects from containers and convert them into specks
	public PumpTileEntity(){
		super(ArcanaTiles.ASPECT_PUMP_TE.get());
		this.direction = Direction.UP;
	}
	
	public PumpTileEntity(Direction direction){
		super(ArcanaTiles.ASPECT_PUMP_TE.get());
		this.direction = direction;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void tick(){
		super.tick();
		// pull new specks
		if(!suppressedByRedstone && specks.size() < 10 && getWorld().getGameTime() % PULL_TIME == 0){
			TileEntity from = getWorld().getTileEntity(getPos().offset(direction.getOpposite()));
			AspectHandler handler = AspectHandler.getFrom(from);
			if(handler != null){
				boolean hasFilter = !filter().isEmpty() && filter().getItem() instanceof EnchantedFilterItem;
				int effBoost = hasFilter ? ((EnchantedFilterItem)filter().getItem()).efficiencyBoost : 0;
				AspectStack stack;
				if(filteredTo() == Aspects.EMPTY)
					stack = handler.drainAny(PULL_AMOUNT + effBoost);
				else
					stack = new AspectStack(filteredTo(), handler.drain(filteredTo(), PULL_AMOUNT + effBoost));
				if(!stack.isEmpty()){
					int speedBoost = hasFilter ? ((EnchantedFilterItem)filter().getItem()).speedBoost : 0;
					addSpeck(new AspectSpeck(stack, SPECK_SPEED + speedBoost * 0.1f, direction, 0));
				}
			}
		}
	}
	
	protected Optional<Direction> redirect(AspectSpeck speck, boolean canPass){
		return (!suppressedByRedstone && (crystal().isEmpty() || filteredTo() == speck.payload.getAspect()))
				? Optional.of(direction)
				: Optional.empty();
	}
	
	public ItemStack filter(){
		return inventory.getStackInSlot(0);
	}
	
	public ItemStack crystal(){
		return inventory.getStackInSlot(1);
	}
	
	public Aspect filteredTo(){
		return crystal().getItem() instanceof CrystalItem ? ((CrystalItem)crystal().getItem()).aspect : Aspects.EMPTY;
	}
	
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (LazyOptional<T>)LazyOptional.of(() -> inventory);
		return super.getCapability(cap, side);
	}
	
	public ITextComponent getDisplayName(){
		return new TranslationTextComponent("block.arcana.essentia_pump");
	}
	
	@Nullable
	public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player){
		return new PumpContainer(id, this, playerInv);
	}
	
	public void read(BlockState state, CompoundNBT nbt){
		super.read(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("items"));
		suppressedByRedstone = nbt.getBoolean("suppressed");
	}
	
	public CompoundNBT write(CompoundNBT compound){
		CompoundNBT nbt = super.write(compound);
		nbt.put("items", inventory.serializeNBT());
		nbt.putBoolean("suppressed", suppressedByRedstone);
		return nbt;
	}
}