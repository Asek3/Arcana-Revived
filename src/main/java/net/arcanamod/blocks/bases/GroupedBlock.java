package net.arcanamod.blocks.bases;

import javax.annotation.Nullable;

import net.minecraft.item.ItemGroup;

public interface GroupedBlock{
	
	@Nullable
	ItemGroup getGroup();
}