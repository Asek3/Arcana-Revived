package net.arcanamod.blocks.bases;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;

// same BS as the sapling block - probably just going to AT these
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ATrapDoorBlock extends TrapDoorBlock{
	
	public ATrapDoorBlock(Block.Properties properties){
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(OPEN, Boolean.FALSE).with(HALF, Half.BOTTOM).with(POWERED, Boolean.FALSE).with(WATERLOGGED, Boolean.valueOf(false)));
	}
}
