package net.arcanamod.items.tools;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.AutoRepair;
import net.minecraft.entity.Entity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutoRepairAxeItem extends AxeItem{
	
	public AutoRepairAxeItem(IItemTier tier, float attackDamage, float attackSpeed, Properties builder){
		super(tier, attackDamage, attackSpeed, builder);
	}
	
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return AutoRepair.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}
	
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack){
		return AutoRepair.shouldCauseBlockBreakReset(oldStack, newStack);
	}
	
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);
		AutoRepair.inventoryTick(stack, world, entity, itemSlot, isSelected);
	}
}