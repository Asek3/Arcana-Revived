package net.arcanamod.worldgen.trees;

import java.util.Random;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.arcanamod.worldgen.ArcanaFeatures;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

@ParametersAreNonnullByDefault
public class SilverwoodTree extends Tree{
	
	@Nullable
	protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random rand, boolean largeHive){
		return ArcanaFeatures.SILVERWOOD_TREE;
	}
}