package net.arcanamod.containers.slots;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.IAspectHolder;
import net.arcanamod.aspects.UndecidedAspectStack;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.arcanamod.util.recipes.IArcaneCraftingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.Optional;

public class AspectCraftingResultSlot extends CraftingResultSlot {
	private final AspectCraftingInventory craftMatrix;
	private final PlayerEntity player;
	public AspectCraftingResultSlot(PlayerEntity player, AspectCraftingInventory aspectCraftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(player, aspectCraftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
		this.craftMatrix = aspectCraftingInventory;
		this.player = player;
	}

	@Override
	public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
		this.onCrafting(stack);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, this.craftMatrix, thePlayer.world);
		Optional<IArcaneCraftingRecipe> optionalRecipe = thePlayer.world.getRecipeManager().getRecipe(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, this.craftMatrix, thePlayer.world);
		net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
		if (optionalRecipe.isPresent()){
			IArcaneCraftingRecipe recipe = optionalRecipe.get();
			UndecidedAspectStack[] aspectStacks = recipe.getAspectStacks();
			if (aspectStacks.length != 0) {
				if (craftMatrix.getWandSlot() != null){
					if (craftMatrix.getWandSlot().getStack() != ItemStack.EMPTY) {
						IAspectHandler handler = IAspectHandler.getFrom(craftMatrix.getWandSlot().getStack());
						this.takeAspects(craftMatrix, handler, aspectStacks);
					}
				}
			}
		}
		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
			ItemStack itemstack1 = nonnulllist.get(i);
			if (!itemstack.isEmpty()) {
				this.craftMatrix.decrStackSize(i, 1);
				itemstack = this.craftMatrix.getStackInSlot(i);
			}

			if (!itemstack1.isEmpty()) {
				if (itemstack.isEmpty()) {
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
					itemstack1.grow(itemstack.getCount());
					this.craftMatrix.setInventorySlotContents(i, itemstack1);
				} else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
					this.player.dropItem(itemstack1, false);
				}
			}
		}

		return stack;
	}

	private boolean takeAspects(AspectCraftingInventory craftMatrix, @Nullable IAspectHandler handler, UndecidedAspectStack[] aspectStacks) {
		if (handler==null) return false;
		if (handler.getHoldersAmount()==0) return false;

		boolean satisfied = true;
		boolean anySatisfied = false;
		boolean hasAny = false;
		for (IAspectHolder holder : handler.getHolders()){
			for (UndecidedAspectStack stack : aspectStacks){
				if (stack.any){
					hasAny = true;
					if (holder.getCurrentVis() >= stack.stack.getAmount()) {
						anySatisfied = true;
						holder.drain(new AspectStack(holder.getContainedAspect(),stack.stack.getAmount()),false);
					}
				}
				else if (holder.getContainedAspect() == stack.stack.getAspect()){
					if (holder.getCurrentVis() >= stack.stack.getAmount()){
						holder.drain(stack.stack,false);
					} else satisfied = false;
				}
			}
		}
		return satisfied && (!hasAny || anySatisfied);
	}
}
