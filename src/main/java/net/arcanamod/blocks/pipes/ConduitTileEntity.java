package net.arcanamod.blocks.pipes;

import java.util.Optional;

import net.arcanamod.aspects.Aspect;
import net.minecraft.util.Direction;

public class ConduitTileEntity extends TubeTileEntity{
	
	// conditionally redirect (types of) specks
	Aspect whitelist = null;
	Direction dir;
	
	protected Optional<Direction> redirect(AspectSpeck speck, boolean canPass){
		if(speck.pos >= .5f && !getWorld().isBlockPowered(pos) && (whitelist == null || speck.payload.getAspect() == whitelist))
			return Optional.of(dir);
		return Optional.empty();
	}
}