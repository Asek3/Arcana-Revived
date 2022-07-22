package net.arcanamod.containers.slots;

import java.util.Optional;

import javax.annotation.Nullable;

import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.aspects.handlers.AspectHolder;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.recipes.ArcanaRecipes;
import net.arcanamod.items.recipes.AspectCraftingInventory;
import net.arcanamod.items.recipes.IArcaneCraftingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

public class AspectCraftingResultSlot extends CraftingResultSlot{
	private final AspectCraftingInventory craftMatrix;
	private final PlayerEntity player;
	
	public AspectCraftingResultSlot(PlayerEntity player, AspectCraftingInventory aspectCraftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition){
		super(player, aspectCraftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.craftMatrix = aspectCraftingInventory;
		this.player = player;
	}
	
	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack){
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonnulllist;
		Optional<IArcaneCraftingRecipe> optionalRecipe = thePlayer.world.getRecipeManager().getRecipe(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, this.craftMatrix, thePlayer.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		if(optionalRecipe.isPresent()){
			nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, this.craftMatrix, thePlayer.world);
			IArcaneCraftingRecipe recipe = optionalRecipe.get();
			UndecidedAspectStack[] aspectStacks = recipe.getAspectStacks();
			if(aspectStacks.length != 0){
				if(craftMatrix.getWandSlot() != null){
					if(craftMatrix.getWandSlot().getStack() != ItemStack.EMPTY){
						AspectHandler handler = AspectHandler.getFrom(craftMatrix.getWandSlot().getStack());
						this.takeAspects(craftMatrix, handler, aspectStacks);
					}
				}
			}
		}else{
			nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(IRecipeType.CRAFTING, this.craftMatrix, thePlayer.world);
		}
		for(int i = 0; i < nonnulllist.size(); ++i){
			ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = nonnulllist.get(i);
			if(!itemstack.isEmpty() && !(itemstack.getItem() instanceof MagicDeviceItem)){
				this.craftMatrix.decrStackSize(i, 1);
				itemstack = this.craftMatrix.getStackInSlot(i);
			}
			
			if(!itemstack1.isEmpty()){
				if(itemstack.isEmpty()){
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				}else if(ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)){
					itemstack1.grow(itemstack.getCount());
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				}else if(!this.player.inventory.addItemStackToInventory(itemstack1)){
					this.player.dropItem(itemstack1, false);
				}
			}
		}
		
		return stack;
	}
	
	private boolean takeAspects(AspectCraftingInventory craftMatrix, @Nullable AspectHandler handler, UndecidedAspectStack[] aspectStacks){
		if(handler == null)
			return false;
		if(handler.countHolders() >= 0)
			return false;
		
		boolean satisfied = true;
		boolean anySatisfied = false;
		boolean hasAny = false;
		for(AspectHolder holder : handler.getHolders()){
			for(UndecidedAspectStack stack : aspectStacks){
				if(stack.any){
					hasAny = true;
					if(holder.getStack().getAmount() >= stack.stack.getAmount()){
						if(!anySatisfied)
							holder.drain(stack.stack.getAmount(), false);
						anySatisfied = true;
					}
				}else if(holder.getStack().getAspect() == stack.stack.getAspect()){
					if(holder.getStack().getAmount() >= stack.stack.getAmount()){
						holder.drain(stack.stack.getAmount(), false);
					}else
						satisfied = false;
				}
			}
		}
		return satisfied && (!hasAny || anySatisfied);
	}
}
