package net.arcanamod.datagen;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_BRICKS_SLAB;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_BRICKS_STAIRS;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_BRICKS_WALL;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_SLAB;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_STAIRS;
import static net.arcanamod.blocks.ArcanaBlocks.ARCANE_STONE_WALL;
import static net.arcanamod.blocks.ArcanaBlocks.CRACKED_DUNGEON_BRICKS_SLAB;
import static net.arcanamod.blocks.ArcanaBlocks.CRACKED_DUNGEON_BRICKS_STAIRS;
import static net.arcanamod.blocks.ArcanaBlocks.CRACKED_DUNGEON_BRICKS_WALL;
import static net.arcanamod.blocks.ArcanaBlocks.DAIR_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.DAIR_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.DEAD_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.DEAD_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.DUNGEON_BRICKS_SLAB;
import static net.arcanamod.blocks.ArcanaBlocks.DUNGEON_BRICKS_STAIRS;
import static net.arcanamod.blocks.ArcanaBlocks.DUNGEON_BRICKS_WALL;
import static net.arcanamod.blocks.ArcanaBlocks.EUCALYPTUS_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.EUCALYPTUS_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.GREATWOOD_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.GREATWOOD_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.HAWTHORN_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.HAWTHORN_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.MOSSY_DUNGEON_BRICKS_SLAB;
import static net.arcanamod.blocks.ArcanaBlocks.MOSSY_DUNGEON_BRICKS_STAIRS;
import static net.arcanamod.blocks.ArcanaBlocks.MOSSY_DUNGEON_BRICKS_WALL;
import static net.arcanamod.blocks.ArcanaBlocks.SILVERWOOD_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.SILVERWOOD_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.SILVER_BLOCK;
import static net.arcanamod.blocks.ArcanaBlocks.SILVER_ORE;
import static net.arcanamod.blocks.ArcanaBlocks.TRYPOPHOBIUS_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.TRYPOPHOBIUS_FENCE_GATE;
import static net.arcanamod.blocks.ArcanaBlocks.VOID_METAL_BLOCK;
import static net.arcanamod.blocks.ArcanaBlocks.WILLOW_FENCE;
import static net.arcanamod.blocks.ArcanaBlocks.WILLOW_FENCE_GATE;
import static net.arcanamod.datagen.ArcanaDataGenerators.DAIR;
import static net.arcanamod.datagen.ArcanaDataGenerators.DEAD;
import static net.arcanamod.datagen.ArcanaDataGenerators.EUCALYPTUS;
import static net.arcanamod.datagen.ArcanaDataGenerators.GREATWOOD;
import static net.arcanamod.datagen.ArcanaDataGenerators.HAWTHORN;
import static net.arcanamod.datagen.ArcanaDataGenerators.SILVERWOOD;
import static net.arcanamod.datagen.ArcanaDataGenerators.TRYPOPHOBIUS;
import static net.arcanamod.datagen.ArcanaDataGenerators.WILLOW;

import javax.annotation.Nonnull;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class Blockstates extends BlockStateProvider{

	ExistingFileHelper efh;
	
	public Blockstates(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, Arcana.MODID, exFileHelper);
		efh = exFileHelper;
	}
	
	protected void registerStatesAndModels(){
		ArcanaDataGenerators.LIVING_WOODS.forEach((name, texture) -> {
			if (ForgeRegistries.BLOCKS.getValue(arcLoc("stripped_" + name + "_wood")) != Blocks.AIR){
			simpleBlock(ForgeRegistries.BLOCKS.getValue(arcLoc("stripped_" + name + "_wood")));
			simpleBlock(ForgeRegistries.BLOCKS.getValue(arcLoc(name+"_wood")));
			logBlock((RotatedPillarBlock) ForgeRegistries.BLOCKS.getValue(arcLoc("stripped_" + name + "_log")));
			}
		});
		
		fenceBlock(DAIR_FENCE.get(), DAIR);
		fenceBlock(DEAD_FENCE.get(), DEAD);
		fenceBlock(EUCALYPTUS_FENCE.get(), EUCALYPTUS);
		fenceBlock(HAWTHORN_FENCE.get(), HAWTHORN);
		fenceBlock(GREATWOOD_FENCE.get(), GREATWOOD);
		fenceBlock(SILVERWOOD_FENCE.get(), SILVERWOOD);
		fenceBlock(TRYPOPHOBIUS_FENCE.get(), TRYPOPHOBIUS);
		fenceBlock(WILLOW_FENCE.get(), WILLOW);
		
		fenceGateBlock(DAIR_FENCE_GATE.get(), DAIR);
		fenceGateBlock(DEAD_FENCE_GATE.get(), DEAD);
		fenceGateBlock(EUCALYPTUS_FENCE_GATE.get(), EUCALYPTUS);
		fenceGateBlock(HAWTHORN_FENCE_GATE.get(), HAWTHORN);
		fenceGateBlock(GREATWOOD_FENCE_GATE.get(), GREATWOOD);
		fenceGateBlock(SILVERWOOD_FENCE_GATE.get(), SILVERWOOD);
		fenceGateBlock(TRYPOPHOBIUS_FENCE_GATE.get(), TRYPOPHOBIUS);
		fenceGateBlock(WILLOW_FENCE_GATE.get(), WILLOW);
		
		simpleBlock(ArcanaBlocks.ARCANE_STONE.get());
		simpleBlock(ArcanaBlocks.ARCANE_STONE_BRICKS.get());
		simpleBlock(ArcanaBlocks.DUNGEON_BRICKS.get());
		simpleBlock(ArcanaBlocks.CRACKED_DUNGEON_BRICKS.get());
		simpleBlock(ArcanaBlocks.MOSSY_DUNGEON_BRICKS.get());
		
		simpleBlock(ArcanaBlocks.TAINTED_GRANITE.get());
		simpleBlock(ArcanaBlocks.TAINTED_DIORITE.get());
		simpleBlock(ArcanaBlocks.TAINTED_ANDESITE.get());
		
		slabBlock(ARCANE_STONE_SLAB.get(), ArcanaDataGenerators.ARCANE_STONE, ArcanaDataGenerators.ARCANE_STONE);
		slabBlock(ARCANE_STONE_BRICKS_SLAB.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS, ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		slabBlock(DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.DUNGEON_BRICKS, ArcanaDataGenerators.DUNGEON_BRICKS);
		slabBlock(CRACKED_DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS, ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		slabBlock(MOSSY_DUNGEON_BRICKS_SLAB.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS, ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		stairsBlock(ARCANE_STONE_STAIRS.get(), ArcanaDataGenerators.ARCANE_STONE);
		stairsBlock(ARCANE_STONE_BRICKS_STAIRS.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		stairsBlock(DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.DUNGEON_BRICKS);
		stairsBlock(CRACKED_DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		stairsBlock(MOSSY_DUNGEON_BRICKS_STAIRS.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		// pressure plate blockstates are done manually
		
		wallBlock(ARCANE_STONE_WALL.get(), ArcanaDataGenerators.ARCANE_STONE);
		wallBlock(ARCANE_STONE_BRICKS_WALL.get(), ArcanaDataGenerators.ARCANE_STONE_BRICKS);
		wallBlock(DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.DUNGEON_BRICKS);
		wallBlock(CRACKED_DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.CRACKED_DUNGEON_BRICKS);
		wallBlock(MOSSY_DUNGEON_BRICKS_WALL.get(), ArcanaDataGenerators.MOSSY_DUNGEON_BRICKS);
		
		simpleBlock(SILVER_BLOCK.get());
		simpleBlock(SILVER_ORE.get());
		simpleBlock(VOID_METAL_BLOCK.get());
	}
	
	@Nonnull
	public String getName(){
		return "Arcana Blockstates";
	}
}