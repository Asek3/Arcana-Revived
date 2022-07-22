package net.arcanamod.client.research;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.arcanamod.client.research.impls.ItemRequirementRenderer;
import net.arcanamod.client.research.impls.ItemTagRequirementRenderer;
import net.arcanamod.client.research.impls.PuzzleRequirementRenderer;
import net.arcanamod.client.research.impls.PuzzlesCompletedRequirementRenderer;
import net.arcanamod.client.research.impls.ResearchCompletedRequirementRenderer;
import net.arcanamod.client.research.impls.XpRequirementRenderer;
import net.arcanamod.systems.research.Requirement;
import net.arcanamod.systems.research.impls.ItemRequirement;
import net.arcanamod.systems.research.impls.ItemTagRequirement;
import net.arcanamod.systems.research.impls.PuzzleRequirement;
import net.arcanamod.systems.research.impls.PuzzlesCompletedRequirement;
import net.arcanamod.systems.research.impls.ResearchCompletedRequirement;
import net.arcanamod.systems.research.impls.XpRequirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface RequirementRenderer<T extends Requirement>{
	
	Map<ResourceLocation, RequirementRenderer<?>> map = new HashMap<>();
	
	static void init(){
		map.put(ItemRequirement.TYPE, new ItemRequirementRenderer());
		map.put(ItemTagRequirement.TYPE, new ItemTagRequirementRenderer());
		map.put(XpRequirement.TYPE, new XpRequirementRenderer());
		map.put(PuzzleRequirement.TYPE, new PuzzleRequirementRenderer());
		map.put(ResearchCompletedRequirement.TYPE, new ResearchCompletedRequirementRenderer());
		map.put(PuzzlesCompletedRequirement.TYPE, new PuzzlesCompletedRequirementRenderer());
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(String type){
		return (RequirementRenderer<T>)map.get(new ResourceLocation(type));
	}
	
	static <T extends Requirement> RequirementRenderer<T> get(Requirement type){
		return (RequirementRenderer<T>)map.get(type.type());
	}
	
	void render(MatrixStack matrices, int x, int y, T requirement, int ticks, float partialTicks, PlayerEntity player);
	
	List<ITextComponent> tooltip(T requirement, PlayerEntity player);
	
	default boolean shouldDrawTickOrCross(T requirement, int amount){
		return amount == 1;
	}
}