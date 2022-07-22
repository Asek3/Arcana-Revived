package net.arcanamod.worldgen.trees;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.block.trees.BigTree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

@ParametersAreNonnullByDefault
public class TaintedGreatwoodTree extends BigTree{
	
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getHugeTreeFeature(Random rand){
		return ArcanaFeatures.TAINTED_GREATWOOD_TREE;
	}
	
	@Override
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean largeHive){
		return null;
	}
}