package net.arcanamod.aspects;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IOverrideAspects{
	
	List<AspectStack> getAspectStacks(ItemStack stack);
}