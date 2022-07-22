package net.arcanamod.blocks.bases;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;

/**
 * SaplingBlock's constructor is protected, for some reason.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ASaplingBlock extends SaplingBlock{
	
	public ASaplingBlock(Tree tree, Block.Properties properties){
		super(tree, properties);
	}
}