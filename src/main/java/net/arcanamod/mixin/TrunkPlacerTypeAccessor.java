package net.arcanamod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

@Mixin(TrunkPlacerType.class)
public interface TrunkPlacerTypeAccessor {
	@Invoker
	static <P extends AbstractTrunkPlacer> TrunkPlacerType<P> createTrunkPlacerType(Codec<P> codec) {
		throw new UnsupportedOperationException();
	}
}