package net.arcanamod.systems.research.impls;

import static net.arcanamod.Arcana.arcLoc;

import net.arcanamod.capabilities.Researcher;
import net.arcanamod.systems.research.Requirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class PuzzlesCompletedRequirement extends Requirement{
	
	public static final ResourceLocation TYPE = arcLoc("puzzles_completed");
	
	public boolean satisfied(PlayerEntity player){
		return Researcher.getFrom(player).getPuzzlesCompleted() >= getAmount();
	}
	
	public void take(PlayerEntity player){
		// no-op
	}
	
	public ResourceLocation type(){
		return TYPE;
	}
	
	public CompoundNBT data(){
		return new CompoundNBT();
	}
}