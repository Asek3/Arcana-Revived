package net.arcanamod.items.armor;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.items.AutoRepair;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutoRepairArmorItem extends ArmorItem{
	
	public AutoRepairArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder){
		super(materialIn, slot, builder);
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