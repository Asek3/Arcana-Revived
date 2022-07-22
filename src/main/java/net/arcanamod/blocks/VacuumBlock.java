package net.arcanamod.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.arcanamod.blocks.tiles.VacuumTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

@SuppressWarnings({"deprecation"})
public class VacuumBlock extends Block{
 
	public VacuumBlock(Properties properties){
		super(properties);
	}
	
	@Nonnull
    @Override
	public BlockRenderType getRenderType(@Nonnull BlockState state){
		return BlockRenderType.INVISIBLE;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new VacuumTileEntity();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
}
