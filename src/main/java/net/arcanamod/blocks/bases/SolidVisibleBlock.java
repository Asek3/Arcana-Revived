package net.arcanamod.blocks.bases;

import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SolidVisibleBlock extends BreakableBlock{
	
	public SolidVisibleBlock(Properties properties){
		super(properties);
	}
	
	@Override
	public int getOpacity(BlockState state, IBlockReader world, BlockPos pos) {
		return world.getMaxLightLevel();
	}
	
}